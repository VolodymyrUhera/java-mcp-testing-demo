package com.demo.website.mcp;

/**
 * Encapsulates the response from the Retro90s MCP server call.
 */
public class McpChatResponse {
    private final boolean success;
    private final String content;
    private final String error;
    private final long timestamp;

    public McpChatResponse(boolean success, String content, String error, long timestamp) {
        this.success = success;
        this.content = content;
        this.error = error;
        this.timestamp = timestamp;
    }

    public static McpChatResponse success(String content) {
        return new McpChatResponse(true, content, null, System.currentTimeMillis());
    }

    public static McpChatResponse error(String errorMessage) {
        return new McpChatResponse(false, null, errorMessage, System.currentTimeMillis());
    }

    public boolean isSuccess() {
        return success;
    }

    public String getContent() {
        return content;
    }

    public String getError() {
        return error;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
