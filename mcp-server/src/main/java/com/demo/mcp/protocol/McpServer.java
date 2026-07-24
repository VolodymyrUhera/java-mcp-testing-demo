package com.demo.mcp.protocol;

import com.demo.mcp.json.JsonParser;
import com.demo.mcp.json.JsonValue;
import com.demo.mcp.tools.PlaywrightManager;
import com.demo.mcp.tools.PlaywrightToolRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.util.*;
import java.util.logging.Logger;

public class McpServer {
    private static final Logger LOGGER = Logger.getLogger(McpServer.class.getName());

    public static void main(String[] args) {
        PlaywrightManager playwrightManager = new PlaywrightManager();
        PlaywrightToolRegistry registry = new PlaywrightToolRegistry(playwrightManager);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintStream writer = System.out;

        LOGGER.info("Starting Java Playwright MCP Server on Stdio...");

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                try {
                    JsonValue request = JsonParser.parse(line);
                    if (!request.isObject()) continue;

                    JsonValue id = request.get("id");
                    JsonValue methodVal = request.get("method");
                    String method = methodVal != null ? methodVal.asString() : "";

                    if ("initialize".equals(method)) {
                        Map<String, JsonValue> result = new LinkedHashMap<>();
                        result.put("protocolVersion", JsonValue.of("2024-11-05"));

                        Map<String, JsonValue> capabilities = new LinkedHashMap<>();
                        capabilities.put("tools", JsonValue.of(Map.of("listChanged", JsonValue.of(false))));
                        result.put("capabilities", JsonValue.of(capabilities));

                        Map<String, JsonValue> serverInfo = new LinkedHashMap<>();
                        serverInfo.put("name", JsonValue.of("java-playwright-mcp-server"));
                        serverInfo.put("version", JsonValue.of("1.0.0"));
                        result.put("serverInfo", JsonValue.of(serverInfo));

                        writer.println(McpMessage.createResponse(id, JsonValue.of(result)));
                        writer.flush();
                    } else if ("tools/list".equals(method)) {
                        Map<String, JsonValue> result = new LinkedHashMap<>();
                        result.put("tools", JsonValue.of(registry.getToolDefinitions()));

                        writer.println(McpMessage.createResponse(id, JsonValue.of(result)));
                        writer.flush();
                    } else if ("tools/call".equals(method)) {
                        JsonValue params = request.get("params");
                        String name = params != null && params.get("name") != null ? params.get("name").asString() : "";
                        JsonValue arguments = params != null ? params.get("arguments") : null;

                        try {
                            String output = registry.executeTool(name, arguments);
                            writer.println(McpMessage.createToolResult(id, output, false));
                        } catch (Exception e) {
                            writer.println(McpMessage.createToolResult(id, "Error executing tool '" + name + "': " + e.getMessage(), true));
                        }
                        writer.flush();
                    } else if ("notifications/initialized".equals(method)) {
                        // Silent acknowledgement for notification
                    } else {
                        writer.println(McpMessage.createError(id, -32601, "Method not found: " + method));
                        writer.flush();
                    }
                } catch (Exception parseErr) {
                    writer.println(McpMessage.createError(JsonValue.nullValue(), -32700, "Parse error: " + parseErr.getMessage()));
                    writer.flush();
                }
            }
        } catch (Exception e) {
            LOGGER.severe("MCP Server fatal error: " + e.getMessage());
        } finally {
            playwrightManager.closeBrowser();
        }
    }
}
