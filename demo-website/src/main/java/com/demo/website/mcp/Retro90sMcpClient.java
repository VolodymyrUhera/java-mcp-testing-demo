package com.demo.website.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service client for communicating with retro90s-mcp HTTP endpoint via JSON-RPC 2.0.
 */
public class Retro90sMcpClient {
    private static final Logger LOGGER = Logger.getLogger(Retro90sMcpClient.class.getName());
    private static final String DEFAULT_PRIMARY_URL = "http://localhost:8080/message";
    private static final String DEFAULT_FALLBACK_URL = "http://localhost:8081/message";

    private final String primaryUrl;
    private final String fallbackUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public Retro90sMcpClient() {
        this(resolvePrimaryUrl(), DEFAULT_FALLBACK_URL);
    }

    public Retro90sMcpClient(String primaryUrl) {
        this(primaryUrl, DEFAULT_FALLBACK_URL);
    }

    public Retro90sMcpClient(String primaryUrl, String fallbackUrl) {
        this(primaryUrl, fallbackUrl, HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build());
    }

    public Retro90sMcpClient(String primaryUrl, String fallbackUrl, HttpClient httpClient) {
        this.primaryUrl = (primaryUrl != null && !primaryUrl.isBlank()) ? primaryUrl.trim() : DEFAULT_PRIMARY_URL;
        this.fallbackUrl = (fallbackUrl != null && !fallbackUrl.isBlank()) ? fallbackUrl.trim() : DEFAULT_FALLBACK_URL;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    private static String resolvePrimaryUrl() {
        String envUrl = System.getenv("RETRO90S_MCP_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            return envUrl.trim();
        }
        return DEFAULT_PRIMARY_URL;
    }

    /**
     * Sends a prompt question to the Retro90s MCP ask90s tool using JSON-RPC 2.0.
     *
     * @param prompt The question or search prompt.
     * @return McpChatResponse representing success or error.
     */
    public McpChatResponse sendPrompt(String prompt) {
        try {
            String safePrompt = (prompt != null) ? prompt : "";
            String escapedPrompt = escapeJson(safePrompt);
            String payload = "{\"jsonrpc\":\"2.0\",\"id\":\"chat-1\",\"method\":\"tools/call\",\"params\":{\"name\":\"ask90s\",\"arguments\":{\"question\":\"" + escapedPrompt + "\"}}}";

            String responseBody = null;
            String lastError = null;

            // Attempt primary endpoint
            try {
                responseBody = postJson(this.primaryUrl, payload);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Primary MCP endpoint failed (" + primaryUrl + "): " + e.getMessage());
                lastError = e.getMessage();
            }

            // Fallback attempt if primary failed and fallback URL is different
            if (responseBody == null && !this.primaryUrl.equals(this.fallbackUrl)) {
                try {
                    LOGGER.info("Attempting fallback MCP endpoint (" + fallbackUrl + ")...");
                    responseBody = postJson(this.fallbackUrl, payload);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Fallback MCP endpoint failed (" + fallbackUrl + "): " + e.getMessage());
                    lastError = e.getMessage();
                }
            }

            if (responseBody == null) {
                return McpChatResponse.error("Connection failed to Retro90s MCP server: " + (lastError != null ? lastError : "Unable to reach endpoint"));
            }

            return parseMcpResponse(responseBody);

        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Unexpected error in Retro90sMcpClient.sendPrompt", t);
            return McpChatResponse.error("Unexpected error calling MCP service: " + t.getMessage());
        }
    }

    private String postJson(String url, String jsonPayload) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new IOException("HTTP status " + response.statusCode() + ": " + response.body());
        }
    }

    private McpChatResponse parseMcpResponse(String jsonBody) {
        if (jsonBody == null || jsonBody.isBlank()) {
            return McpChatResponse.error("Empty response received from MCP server");
        }

        try {
            JsonNode root = objectMapper.readTree(jsonBody);

            if (root.has("error") && !root.get("error").isNull()) {
                JsonNode errNode = root.get("error");
                String errMsg = errNode.has("message") ? errNode.get("message").asText() : errNode.toString();
                return McpChatResponse.error("MCP RPC Error: " + errMsg);
            }

            if (root.has("result") && !root.get("result").isNull()) {
                JsonNode result = root.get("result");

                // 1. result.content[0].text
                if (result.has("content") && result.get("content").isArray() && result.get("content").size() > 0) {
                    JsonNode firstContent = result.get("content").get(0);
                    if (firstContent.has("text") && !firstContent.get("text").isNull()) {
                        return McpChatResponse.success(firstContent.get("text").asText());
                    }
                }

                // 2. result.text
                if (result.has("text") && !result.get("text").isNull()) {
                    return McpChatResponse.success(result.get("text").asText());
                }

                // 3. result.answer
                if (result.has("answer") && !result.get("answer").isNull()) {
                    return McpChatResponse.success(result.get("answer").asText());
                }

                // 4. result is string
                if (result.isTextual()) {
                    return McpChatResponse.success(result.asText());
                }

                // 5. fallback to result object string
                return McpChatResponse.success(result.toString());
            }

            // If no result or error field, fallback to raw response or string extraction
            String extracted = extractRawTextFallback(jsonBody);
            if (extracted != null && !extracted.isBlank()) {
                return McpChatResponse.success(extracted);
            }

            return McpChatResponse.error("Invalid MCP JSON-RPC response format");

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to parse JSON response using Jackson, attempting fallback: " + e.getMessage());
            String extracted = extractRawTextFallback(jsonBody);
            if (extracted != null && !extracted.isBlank()) {
                return McpChatResponse.success(extracted);
            }
            return McpChatResponse.error("Failed to parse response: " + e.getMessage());
        }
    }

    private String extractRawTextFallback(String json) {
        if (json == null || json.isBlank()) return null;
        int textIdx = json.indexOf("\"text\":");
        if (textIdx != -1) {
            int startQuote = json.indexOf("\"", textIdx + 7);
            if (startQuote != -1) {
                int endQuote = findEndQuote(json, startQuote + 1);
                if (endQuote != -1) {
                    return unescapeJson(json.substring(startQuote + 1, endQuote));
                }
            }
        }
        return json.trim();
    }

    private int findEndQuote(String str, int startIdx) {
        boolean escaped = false;
        for (int i = startIdx; i < str.length(); i++) {
            char c = str.charAt(i);
            if (escaped) {
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                return i;
            }
        }
        return -1;
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    private String unescapeJson(String input) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder();
        int len = input.length();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            if (c == '\\' && i + 1 < len) {
                char next = input.charAt(i + 1);
                switch (next) {
                    case '"' -> { sb.append('"'); i++; }
                    case '\\' -> { sb.append('\\'); i++; }
                    case '/' -> { sb.append('/'); i++; }
                    case 'b' -> { sb.append('\b'); i++; }
                    case 'f' -> { sb.append('\f'); i++; }
                    case 'n' -> { sb.append('\n'); i++; }
                    case 'r' -> { sb.append('\r'); i++; }
                    case 't' -> { sb.append('\t'); i++; }
                    case 'u' -> {
                        if (i + 5 < len) {
                            String hex = input.substring(i + 2, i + 6);
                            try {
                                sb.append((char) Integer.parseInt(hex, 16));
                                i += 5;
                            } catch (NumberFormatException e) {
                                sb.append('\\');
                            }
                        } else {
                            sb.append('\\');
                        }
                    }
                    default -> sb.append('\\');
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String getPrimaryUrl() {
        return primaryUrl;
    }

    public String getFallbackUrl() {
        return fallbackUrl;
    }
}
