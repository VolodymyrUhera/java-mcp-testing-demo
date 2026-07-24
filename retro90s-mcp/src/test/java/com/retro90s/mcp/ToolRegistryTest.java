package com.retro90s.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ToolRegistryTest {

    private ToolRegistry toolRegistry;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        KnowledgeService knowledgeService = new KnowledgeService();
        SearchService searchService = new SearchService();
        toolRegistry = new ToolRegistry(knowledgeService, searchService);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testListToolsReturnsAllNineTools() {
        JsonNode tools = toolRegistry.listTools();
        assertNotNull(tools, "tools list should not be null");
        assertTrue(tools.isArray(), "tools should be an array node");
        assertEquals(9, tools.size(), "tools array should contain exactly 9 tools");

        Set<String> expectedTools = Set.of(
            "ask90s", "compare", "recommend", "explain",
            "trivia", "nostalgia", "year", "website", "hardware"
        );

        Set<String> actualTools = new HashSet<>();
        for (JsonNode tool : tools) {
            assertTrue(tool.has("name"), "Tool must have a name field");
            assertTrue(tool.has("description"), "Tool must have a description field");
            assertTrue(tool.has("inputSchema"), "Tool must have an inputSchema field");

            JsonNode inputSchema = tool.get("inputSchema");
            assertEquals("object", inputSchema.get("type").asText());
            assertTrue(inputSchema.has("properties"));

            actualTools.add(tool.get("name").asText());
        }

        assertEquals(expectedTools, actualTools, "Tool registry must contain all 9 expected tools");
    }

    @Test
    public void testCallAsk90sTool() {
        JsonNode args = objectMapper.createObjectNode().put("question", "Windows 95");
        JsonNode result = toolRegistry.callTool("ask90s", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));
        assertTrue(result.has("content"));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.toLowerCase().contains("windows 95") || text.toLowerCase().contains("cyber-steve"));
    }

    @Test
    public void testCallCompareTool() {
        JsonNode args = objectMapper.createObjectNode()
            .put("left", "DOOM")
            .put("right", "Quake");
        JsonNode result = toolRegistry.callTool("compare", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("DOOM"));
        assertTrue(text.contains("Quake"));
    }

    @Test
    public void testCallRecommendTool() {
        JsonNode args = objectMapper.createObjectNode().put("category", "games");
        JsonNode result = toolRegistry.callTool("recommend", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("Cyber-Steve's Fly Recommendations"));
    }

    @Test
    public void testCallExplainTool() {
        JsonNode args = objectMapper.createObjectNode().put("topic", "Java");
        JsonNode result = toolRegistry.callTool("explain", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("Deep Dive"));
        assertTrue(text.toLowerCase().contains("java"));
    }

    @Test
    public void testCallTriviaTool() {
        JsonNode args = objectMapper.createObjectNode().put("category", "consoles");
        JsonNode result = toolRegistry.callTool("trivia", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("90s Cyber Trivia Time"));
    }

    @Test
    public void testCallNostalgiaTool() {
        JsonNode args = objectMapper.createObjectNode().put("theme", "Dial-up Era");
        JsonNode result = toolRegistry.callTool("nostalgia", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("Nostalgia Time Machine"));
        assertTrue(text.contains("Dial-up Era"));
    }

    @Test
    public void testCallYearTool() {
        JsonNode args = objectMapper.createObjectNode().put("year", 1995);
        JsonNode result = toolRegistry.callTool("year", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("1995"));
    }

    @Test
    public void testCallYearToolInvalidYear() {
        JsonNode args = objectMapper.createObjectNode().put("year", 2010);
        JsonNode result = toolRegistry.callTool("year", args);

        assertNotNull(result);
        assertTrue(result.path("isError").asBoolean(true));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("1990s decade"));
    }

    @Test
    public void testCallWebsiteTool() {
        JsonNode args = objectMapper.createObjectNode().put("name", "GeoCities");
        JsonNode result = toolRegistry.callTool("website", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.toLowerCase().contains("geocities") || text.contains("Information Superhighway"));
    }

    @Test
    public void testCallHardwareTool() {
        JsonNode args = objectMapper.createObjectNode().put("component", "3dfx Voodoo");
        JsonNode result = toolRegistry.callTool("hardware", args);

        assertNotNull(result);
        assertFalse(result.path("isError").asBoolean(false));

        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.toLowerCase().contains("voodoo") || text.contains("Hardware Spec Sheet"));
    }

    @Test
    public void testCallUnknownTool() {
        JsonNode result = toolRegistry.callTool("non_existent_tool", objectMapper.createObjectNode());

        assertNotNull(result);
        assertTrue(result.path("isError").asBoolean(false));
        String text = result.path("content").get(0).path("text").asText();
        assertTrue(text.contains("Unknown tool"));
    }
}
