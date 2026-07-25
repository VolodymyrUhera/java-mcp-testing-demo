package com.demo.website.mcp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Retro90sMcpClientTest {

    @Test
    public void testMcpChatResponseSuccessFactory() {
        McpChatResponse response = McpChatResponse.success("Hello 90s!");
        assertTrue(response.isSuccess());
        assertEquals("Hello 90s!", response.getContent());
        assertNull(response.getError());
        assertTrue(response.getTimestamp() > 0);
    }

    @Test
    public void testMcpChatResponseErrorFactory() {
        McpChatResponse response = McpChatResponse.error("Connection failed");
        assertFalse(response.isSuccess());
        assertNull(response.getContent());
        assertEquals("Connection failed", response.getError());
        assertTrue(response.getTimestamp() > 0);
    }

    @Test
    public void testClientInitializationDefaults() {
        Retro90sMcpClient client = new Retro90sMcpClient();
        assertNotNull(client.getPrimaryUrl());
        assertEquals("http://localhost:8081/message", client.getFallbackUrl());
    }

    @Test
    public void testClientCustomUrl() {
        Retro90sMcpClient client = new Retro90sMcpClient("http://custom:9090/message", "http://fallback:9091/message");
        assertEquals("http://custom:9090/message", client.getPrimaryUrl());
        assertEquals("http://fallback:9091/message", client.getFallbackUrl());
    }

    @Test
    public void testSendPromptWhenServerOfflineReturnsErrorGracefully() {
        Retro90sMcpClient client = new Retro90sMcpClient("http://localhost:59999/message", "http://localhost:59998/message");
        McpChatResponse response = client.sendPrompt("Test question");
        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertTrue(response.getError().contains("failed") || response.getError().contains("Connection"));
    }
}
