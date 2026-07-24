package com.retro90s.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Embedded HTTP SSE MCP server listening on configurable port (default 8080).
 * Serves /sse and /message endpoints processing JSON-RPC 2.0 requests.
 */
public class Retro90sServer {

    private final int port;
    private final KnowledgeService knowledgeService;
    private final SearchService searchService;
    private final ToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;
    private final Map<String, OutputStream> activeSseSessions;

    private HttpServer server;

    public Retro90sServer() {
        this(8080);
    }

    public Retro90sServer(int port) {
        this(port, new KnowledgeService(), new SearchService());
    }

    public Retro90sServer(int port, KnowledgeService knowledgeService, SearchService searchService) {
        this(port, knowledgeService, searchService, new ToolRegistry(knowledgeService, searchService));
    }

    public Retro90sServer(int port, KnowledgeService knowledgeService, SearchService searchService, ToolRegistry toolRegistry) {
        this.port = port;
        this.knowledgeService = Objects.requireNonNull(knowledgeService, "KnowledgeService must not be null");
        this.searchService = Objects.requireNonNull(searchService, "SearchService must not be null");
        this.toolRegistry = Objects.requireNonNull(toolRegistry, "ToolRegistry must not be null");
        this.objectMapper = new ObjectMapper();
        this.activeSseSessions = new ConcurrentHashMap<>();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        server.createContext("/sse", new SseHandler());
        server.createContext("/message", new MessageHandler());

        server.start();
        System.out.println("🕹️ Retro90s MCP Server started on port " + getPort());
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Stopped Retro90s MCP Server.");
        }
    }

    public int getPort() {
        if (server != null) {
            return server.getAddress().getPort();
        }
        return port;
    }

    private class SseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCorsPreflight(exchange);
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            String sessionId = UUID.randomUUID().toString();
            exchange.getResponseHeaders().set("Content-Type", "text/event-stream");
            exchange.getResponseHeaders().set("Cache-Control", "no-cache");
            exchange.getResponseHeaders().set("Connection", "keep-alive");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            activeSseSessions.put(sessionId, os);

            String endpointEvent = "event: endpoint\ndata: /message?sessionId=" + sessionId + "\n\n";
            os.write(endpointEvent.getBytes(StandardCharsets.UTF_8));
            os.flush();

            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(15000);
                    os.write(": ping\n\n".getBytes(StandardCharsets.UTF_8));
                    os.flush();
                }
            } catch (Exception e) {
                // Connection closed or interrupted
            } finally {
                activeSseSessions.remove(sessionId);
                try {
                    os.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private class MessageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCorsPreflight(exchange);
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream is = exchange.getRequestBody();
            String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            if (requestBody.isBlank()) {
                sendJsonResponse(exchange, 400, createErrorResponse(null, -32700, "Parse error: Empty request body"));
                return;
            }

            JsonNode requestJson;
            try {
                requestJson = objectMapper.readTree(requestBody);
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, createErrorResponse(null, -32700, "Parse error: Invalid JSON"));
                return;
            }

            JsonNode responseJson = processJsonRpcRequest(requestJson);

            sendJsonResponse(exchange, 200, responseJson);

            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("sessionId=")) {
                String sessionId = extractSessionId(query);
                if (sessionId != null && activeSseSessions.containsKey(sessionId)) {
                    OutputStream os = activeSseSessions.get(sessionId);
                    try {
                        String messageEvent = "event: message\ndata: " + objectMapper.writeValueAsString(responseJson) + "\n\n";
                        os.write(messageEvent.getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    private String extractSessionId(String query) {
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && "sessionId".equalsIgnoreCase(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    private JsonNode processJsonRpcRequest(JsonNode req) {
        JsonNode idNode = req.get("id");
        String method = req.has("method") ? req.get("method").asText() : "";
        JsonNode params = req.get("params");

        return switch (method) {
            case "initialize" -> handleInitialize(idNode);
            case "ping" -> handlePing(idNode);
            case "tools/list" -> handleToolsList(idNode);
            case "tools/call" -> handleToolsCall(idNode, params);
            case "resources/list" -> handleResourcesList(idNode);
            case "resources/read" -> handleResourcesRead(idNode, params);
            case "prompts/list" -> handlePromptsList(idNode);
            case "prompts/get" -> handlePromptsGet(idNode, params);
            default -> createErrorResponse(idNode, -32601, "Method not found: " + method);
        };
    }

    private JsonNode handleInitialize(JsonNode id) {
        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);

        ObjectNode result = objectMapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");

        ObjectNode capabilities = objectMapper.createObjectNode();
        capabilities.set("tools", objectMapper.createObjectNode());
        capabilities.set("resources", objectMapper.createObjectNode());
        capabilities.set("prompts", objectMapper.createObjectNode());
        result.set("capabilities", capabilities);

        ObjectNode serverInfo = objectMapper.createObjectNode();
        serverInfo.put("name", "retro90s-mcp");
        serverInfo.put("version", "1.0.0");
        result.set("serverInfo", serverInfo);

        res.set("result", result);
        return res;
    }

    private JsonNode handlePing(JsonNode id) {
        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);
        res.set("result", objectMapper.createObjectNode());
        return res;
    }

    private JsonNode handleToolsList(JsonNode id) {
        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);

        ObjectNode result = objectMapper.createObjectNode();
        result.set("tools", toolRegistry.listTools());
        res.set("result", result);
        return res;
    }

    private JsonNode handleToolsCall(JsonNode id, JsonNode params) {
        if (params == null || !params.has("name")) {
            return createErrorResponse(id, -32602, "Invalid params: 'name' is required");
        }
        String toolName = params.get("name").asText();
        JsonNode args = params.get("arguments");

        JsonNode toolResult = toolRegistry.callTool(toolName, args);

        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);
        res.set("result", toolResult);
        return res;
    }

    private JsonNode handleResourcesList(JsonNode id) {
        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);

        ArrayNode resources = objectMapper.createArrayNode();

        resources.add(createResourceNode(
            "retro90s://timeline",
            "90s Timeline",
            "Chronological index of 1990s technology, software, hardware, and culture events",
            "application/json"
        ));
        resources.add(createResourceNode(
            "retro90s://operating-systems",
            "1990s Operating Systems",
            "Index of 90s OS releases including Windows, Linux, and MS-DOS",
            "application/json"
        ));
        resources.add(createResourceNode(
            "retro90s://consoles",
            "1990s Video Game Consoles",
            "Index of 90s console gaming hardware (SNES, Genesis, PS1, N64, Dreamcast)",
            "application/json"
        ));
        resources.add(createResourceNode(
            "retro90s://programming",
            "1990s Programming Languages & Tools",
            "Index of 90s programming languages (Java, Python, C++, Visual Basic, Perl, PHP)",
            "application/json"
        ));
        resources.add(createResourceNode(
            "retro90s://internet",
            "1990s Internet & Web History",
            "Index of early web portals, dot-com sites, browsers, and net culture",
            "application/json"
        ));

        ObjectNode result = objectMapper.createObjectNode();
        result.set("resources", resources);
        res.set("result", result);
        return res;
    }

    private JsonNode handleResourcesRead(JsonNode id, JsonNode params) {
        if (params == null || !params.has("uri")) {
            return createErrorResponse(id, -32602, "Invalid params: 'uri' is required");
        }
        String uri = params.get("uri").asText();
        List<KnowledgeItem> items;

        switch (uri.toLowerCase()) {
            case "retro90s://timeline" -> items = knowledgeService.getTimeline();
            case "retro90s://operating-systems" -> items = knowledgeService.getOperatingSystems();
            case "retro90s://consoles" -> items = knowledgeService.getConsoles();
            case "retro90s://programming" -> items = knowledgeService.getProgramming();
            case "retro90s://internet" -> items = knowledgeService.getInternet();
            default -> {
                return createErrorResponse(id, -32602, "Unknown resource URI: " + uri);
            }
        }

        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);

        ArrayNode contents = objectMapper.createArrayNode();
        ObjectNode contentItem = objectMapper.createObjectNode();
        contentItem.put("uri", uri);
        contentItem.put("mimeType", "application/json");
        try {
            contentItem.put("text", objectMapper.writeValueAsString(items));
        } catch (Exception e) {
            contentItem.put("text", "[]");
        }
        contents.add(contentItem);

        ObjectNode result = objectMapper.createObjectNode();
        result.set("contents", contents);
        res.set("result", result);
        return res;
    }

    private JsonNode handlePromptsList(JsonNode id) {
        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);

        ArrayNode prompts = objectMapper.createArrayNode();
        ObjectNode prompt = objectMapper.createObjectNode();
        prompt.put("name", "personality");
        prompt.put("description", "Cyber-Steve retro 90s expert persona system prompt");
        prompt.set("arguments", objectMapper.createArrayNode());
        prompts.add(prompt);

        ObjectNode result = objectMapper.createObjectNode();
        result.set("prompts", prompts);
        res.set("result", result);
        return res;
    }

    private JsonNode handlePromptsGet(JsonNode id, JsonNode params) {
        if (params == null || !params.has("name")) {
            return createErrorResponse(id, -32602, "Invalid params: 'name' is required");
        }
        String name = params.get("name").asText();
        if (!"personality".equalsIgnoreCase(name) && !"personality.md".equalsIgnoreCase(name)) {
            return createErrorResponse(id, -32602, "Unknown prompt name: " + name);
        }

        String promptText = knowledgeService.getPersonalityPrompt();

        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);

        ObjectNode result = objectMapper.createObjectNode();
        result.put("description", "Cyber-Steve 90s expert personality prompt");

        ArrayNode messages = objectMapper.createArrayNode();
        ObjectNode msg = objectMapper.createObjectNode();
        msg.put("role", "user");
        ObjectNode content = objectMapper.createObjectNode();
        content.put("type", "text");
        content.put("text", promptText);
        msg.set("content", content);
        messages.add(msg);

        result.set("messages", messages);
        res.set("result", result);
        return res;
    }

    private ObjectNode createResourceNode(String uri, String name, String description, String mimeType) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("uri", uri);
        node.put("name", name);
        node.put("description", description);
        node.put("mimeType", mimeType);
        return node;
    }

    private ObjectNode createErrorResponse(JsonNode id, int code, String message) {
        ObjectNode res = objectMapper.createObjectNode();
        res.put("jsonrpc", "2.0");
        if (id != null) res.set("id", id);
        ObjectNode err = objectMapper.createObjectNode();
        err.put("code", code);
        err.put("message", message);
        res.set("error", err);
        return res;
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, JsonNode bodyJson) throws IOException {
        byte[] bytes = objectMapper.writeValueAsBytes(bodyJson);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendCorsPreflight(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.sendResponseHeaders(204, -1);
    }
}
