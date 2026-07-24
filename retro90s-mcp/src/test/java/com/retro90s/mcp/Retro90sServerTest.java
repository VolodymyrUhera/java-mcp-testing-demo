package com.retro90s.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class Retro90sServerTest {

    private static Retro90sServer server;
    private static int port;
    private static HttpClient client;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setUp() throws Exception {
        KnowledgeService knowledgeService = new KnowledgeService();
        SearchService searchService = new SearchService();
        ToolRegistry toolRegistry = new ToolRegistry(knowledgeService, searchService);

        server = new Retro90sServer(0, knowledgeService, searchService, toolRegistry);
        server.start();
        port = server.getPort();

        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testSseEndpoint() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/sse"))
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofInputStream()
        );

        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").contains("text/event-stream"));

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8));
        String line1 = reader.readLine();
        String line2 = reader.readLine();

        assertNotNull(line1);
        assertNotNull(line2);
        assertTrue(line1.contains("event: endpoint"));
        assertTrue(line2.contains("data: /message?sessionId="));
    }

    @Test
    public void testInitialize() throws Exception {
        String reqBody = """
                {
                    "jsonrpc": "2.0",
                    "id": 1,
                    "method": "initialize"
                }
                """;

        JsonNode res = postMessage(reqBody);
        assertEquals("2.0", res.get("jsonrpc").asText());
        assertEquals(1, res.get("id").asInt());

        JsonNode result = res.get("result");
        assertNotNull(result);
        assertEquals("2024-11-05", result.get("protocolVersion").asText());
        assertEquals("retro90s-mcp", result.get("serverInfo").get("name").asText());
    }

    @Test
    public void testPing() throws Exception {
        String reqBody = """
                {
                    "jsonrpc": "2.0",
                    "id": 2,
                    "method": "ping"
                }
                """;

        JsonNode res = postMessage(reqBody);
        assertEquals("2.0", res.get("jsonrpc").asText());
        assertEquals(2, res.get("id").asInt());
        assertNotNull(res.get("result"));
    }

    @Test
    public void testToolsList() throws Exception {
        String reqBody = """
                {
                    "jsonrpc": "2.0",
                    "id": 3,
                    "method": "tools/list"
                }
                """;

        JsonNode res = postMessage(reqBody);
        JsonNode tools = res.get("result").get("tools");
        assertNotNull(tools);
        assertTrue(tools.isArray());
        assertTrue(tools.size() >= 9);

        boolean foundAsk90s = false;
        for (JsonNode tool : tools) {
            if ("ask90s".equals(tool.get("name").asText())) {
                foundAsk90s = true;
                break;
            }
        }
        assertTrue(foundAsk90s, "ask90s tool should be present in tools/list");
    }

    @Test
    public void testToolsCall() throws Exception {
        String reqBody = """
                {
                    "jsonrpc": "2.0",
                    "id": 4,
                    "method": "tools/call",
                    "params": {
                        "name": "ask90s",
                        "arguments": {
                            "question": "Windows 95"
                        }
                    }
                }
                """;

        JsonNode res = postMessage(reqBody);
        JsonNode result = res.get("result");
        assertNotNull(result);
        JsonNode content = result.get("content");
        assertNotNull(content);
        assertTrue(content.isArray());
        String text = content.get(0).get("text").asText();
        assertTrue(text.contains("Windows 95"));
    }

    @Test
    public void testResourcesList() throws Exception {
        String reqBody = """
                {
                    "jsonrpc": "2.0",
                    "id": 5,
                    "method": "resources/list"
                }
                """;

        JsonNode res = postMessage(reqBody);
        JsonNode resources = res.get("result").get("resources");
        assertNotNull(resources);
        assertTrue(resources.isArray());
        assertEquals(5, resources.size());
    }

    @Test
    public void testResourcesRead() throws Exception {
        String reqBody = """
                {
                    "jsonrpc": "2.0",
                    "id": 6,
                    "method": "resources/read",
                    "params": {
                        "uri": "retro90s://timeline"
                    }
                }
                """;

        JsonNode res = postMessage(reqBody);
        JsonNode contents = res.get("result").get("contents");
        assertNotNull(contents);
        assertTrue(contents.isArray());
        String uri = contents.get(0).get("uri").asText();
        assertEquals("retro90s://timeline", uri);
        String text = contents.get(0).get("text").asText();
        assertNotNull(text);
        assertTrue(text.startsWith("[") && text.endsWith("]"));
    }

    @Test
    public void testPromptsListAndGet() throws Exception {
        String listReq = """
                {
                    "jsonrpc": "2.0",
                    "id": 7,
                    "method": "prompts/list"
                }
                """;

        JsonNode listRes = postMessage(listReq);
        JsonNode prompts = listRes.get("result").get("prompts");
        assertNotNull(prompts);
        assertTrue(prompts.isArray());
        assertEquals("personality", prompts.get(0).get("name").asText());

        String getReq = """
                {
                    "jsonrpc": "2.0",
                    "id": 8,
                    "method": "prompts/get",
                    "params": {
                        "name": "personality"
                    }
                }
                """;

        JsonNode getRes = postMessage(getReq);
        JsonNode messages = getRes.get("result").get("messages");
        assertNotNull(messages);
        assertTrue(messages.isArray());
        String promptText = messages.get(0).get("content").get("text").asText();
        assertNotNull(promptText);
        assertTrue(promptText.toLowerCase().contains("retro") || promptText.toLowerCase().contains("expert"));
    }

    private JsonNode postMessage(String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/message"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        return objectMapper.readTree(response.body());
    }
}
