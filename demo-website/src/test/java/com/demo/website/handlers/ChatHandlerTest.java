package com.demo.website.handlers;

import com.demo.website.mcp.McpChatResponse;
import com.demo.website.mcp.Retro90sMcpClient;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ChatHandlerTest {

    @Test
    public void testGetChatRendersHtml() throws Exception {
        ChatHandler handler = new ChatHandler();
        MockHttpExchange exchange = new MockHttpExchange("GET", "/chat", "");

        handler.handle(exchange);

        assertEquals(200, exchange.responseCode);
        assertEquals("text/html; charset=UTF-8", exchange.getResponseHeaders().getFirst("Content-Type"));
        String html = exchange.getResponseBodyAsString();
        assertTrue(html.contains("Cyber-Steve 90s AI Chat"));
        assertTrue(html.contains("id=\"chatHistory\""));
        assertTrue(html.contains("id=\"chatInput\""));
        assertTrue(html.contains("id=\"sendBtn\""));
        assertTrue(html.contains("id=\"clearBtn\""));
        assertTrue(html.contains("id=\"typingIndicator\""));
        assertTrue(html.contains("id=\"errorBanner\""));
        assertTrue(html.contains("Skip to main content"));
    }

    @Test
    public void testPostApiChatSuccess() throws Exception {
        Retro90sMcpClient mockClient = new Retro90sMcpClient() {
            @Override
            public McpChatResponse sendPrompt(String prompt) {
                return McpChatResponse.success("Retro response to: " + prompt);
            }
        };
        ChatHandler handler = new ChatHandler(mockClient);

        String jsonInput = "{\"message\":\"Hello 90s\"}";
        MockHttpExchange exchange = new MockHttpExchange("POST", "/api/chat", jsonInput);

        handler.handle(exchange);

        assertEquals(200, exchange.responseCode);
        assertEquals("application/json; charset=UTF-8", exchange.getResponseHeaders().getFirst("Content-Type"));
        String jsonOutput = exchange.getResponseBodyAsString();
        assertTrue(jsonOutput.contains("\"success\":true"));
        assertTrue(jsonOutput.contains("Retro response to: Hello 90s"));
    }

    @Test
    public void testPostApiChatSanitizesInput() throws Exception {
        final String[] receivedPrompt = new String[1];
        Retro90sMcpClient mockClient = new Retro90sMcpClient() {
            @Override
            public McpChatResponse sendPrompt(String prompt) {
                receivedPrompt[0] = prompt;
                return McpChatResponse.success("OK");
            }
        };
        ChatHandler handler = new ChatHandler(mockClient);

        String jsonInput = "{\"message\":\"<b id=\\\"test\\\">Hi</b>\"}";
        MockHttpExchange exchange = new MockHttpExchange("POST", "/api/chat", jsonInput);

        handler.handle(exchange);

        assertEquals(200, exchange.responseCode);
        assertEquals("&lt;b id=&quot;test&quot;&gt;Hi&lt;/b&gt;", receivedPrompt[0]);
    }

    private static class MockHttpExchange extends HttpExchange {
        private final String method;
        private final URI uri;
        private final InputStream requestBody;
        private final ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        private final Headers requestHeaders = new Headers();
        private final Headers responseHeaders = new Headers();
        private int responseCode;

        public MockHttpExchange(String method, String path, String requestBodyText) {
            this.method = method;
            this.uri = URI.create(path);
            this.requestBody = new ByteArrayInputStream(requestBodyText.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public Headers getRequestHeaders() { return requestHeaders; }
        @Override
        public Headers getResponseHeaders() { return responseHeaders; }
        @Override
        public URI getRequestURI() { return uri; }
        @Override
        public String getRequestMethod() { return method; }
        @Override
        public HttpContext getHttpContext() { return null; }
        @Override
        public void close() {}
        @Override
        public InputStream getRequestBody() { return requestBody; }
        @Override
        public OutputStream getResponseBody() { return responseBody; }
        @Override
        public void sendResponseHeaders(int rCode, long responseLength) { this.responseCode = rCode; }
        @Override
        public int getResponseCode() { return responseCode; }
        @Override
        public InetSocketAddress getRemoteAddress() { return null; }
        @Override
        public InetSocketAddress getLocalAddress() { return null; }
        @Override
        public String getProtocol() { return "HTTP/1.1"; }
        @Override
        public Object getAttribute(String name) { return null; }
        @Override
        public void setAttribute(String name, Object value) {}
        @Override
        public void setStreams(InputStream i, OutputStream o) {}
        @Override
        public HttpPrincipal getPrincipal() { return null; }

        public String getResponseBodyAsString() {
            return responseBody.toString(StandardCharsets.UTF_8);
        }
    }
}
