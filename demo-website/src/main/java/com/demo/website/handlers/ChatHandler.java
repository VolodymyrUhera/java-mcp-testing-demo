package com.demo.website.handlers;

import com.demo.website.mcp.McpChatResponse;
import com.demo.website.mcp.Retro90sMcpClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ChatHandler implements HttpHandler {
    private final Retro90sMcpClient mcpClient;
    private final ObjectMapper objectMapper;

    public ChatHandler() {
        this(new Retro90sMcpClient());
    }

    public ChatHandler(Retro90sMcpClient mcpClient) {
        this.mcpClient = mcpClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if ("POST".equalsIgnoreCase(method)) {
            handlePost(exchange);
        } else {
            handleGet(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String message = extractMessage(body);
        String sanitizedMessage = sanitizeHtml(message);

        McpChatResponse response = mcpClient.sendPrompt(sanitizedMessage);

        long timestamp = (response != null && response.getTimestamp() > 0)
                ? response.getTimestamp()
                : System.currentTimeMillis();

        String jsonResponse;
        if (response != null && response.isSuccess()) {
            String reply = response.getContent() != null ? response.getContent() : "";
            String escapedReply = sanitizeHtml(reply);

            Map<String, Object> payload = new HashMap<>();
            payload.put("success", true);
            payload.put("reply", escapedReply);
            payload.put("timestamp", timestamp);
            jsonResponse = objectMapper.writeValueAsString(payload);
        } else {
            String errorMsg = (response != null && response.getError() != null)
                    ? response.getError()
                    : "Failed to communicate with AI server";
            String escapedError = sanitizeHtml(errorMsg);

            Map<String, Object> payload = new HashMap<>();
            payload.put("success", false);
            payload.put("error", escapedError);
            payload.put("timestamp", timestamp);
            jsonResponse = objectMapper.writeValueAsString(payload);
        }

        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private String extractMessage(String body) {
        if (body == null || body.isBlank()) {
            return "";
        }
        try {
            JsonNode node = objectMapper.readTree(body);
            if (node != null && node.has("message")) {
                return node.get("message").asText("");
            }
        } catch (Exception ignored) {
            int idx = body.indexOf("\"message\"");
            if (idx != -1) {
                int startQuote = body.indexOf("\"", idx + 9);
                if (startQuote != -1) {
                    int endQuote = body.indexOf("\"", startQuote + 1);
                    if (endQuote != -1) {
                        return body.substring(startQuote + 1, endQuote);
                    }
                }
            }
        }
        return "";
    }

    private String sanitizeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;");
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String sidebar = NavigationHelper.renderSidebar("/chat");

        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Cyber Chat 90s - CyberSpace 1999</title>
                <link rel="stylesheet" href="/static/style.css">
                <style>
                    #chatHistory {
                        background-color: #ffffff;
                        color: #000000;
                        border: 2px inset #c0c0c0;
                        height: 320px;
                        overflow-y: auto;
                        padding: 10px;
                        margin-bottom: 10px;
                        font-family: "Courier New", monospace;
                        font-size: 13px;
                    }
                    .chat-bubble {
                        margin-bottom: 10px;
                        padding: 8px 12px;
                        border-radius: 4px;
                        border: 2px outset #c0c0c0;
                    }
                    .user-bubble {
                        background-color: #e6f2ff;
                        border-color: #000080;
                    }
                    .assistant-bubble {
                        background-color: #e6ffe6;
                        border-color: #008000;
                    }
                    .bubble-meta {
                        font-size: 11px;
                        color: #333333;
                        margin-bottom: 4px;
                    }
                    .bubble-content {
                        white-space: pre-wrap;
                        word-break: break-word;
                    }
                    #typingIndicator {
                        font-style: italic;
                        color: #000080;
                        margin-bottom: 10px;
                        font-weight: bold;
                    }
                    #errorBanner {
                        color: #cc0000;
                        background-color: #ffcccc;
                        border: 2px inset #cc0000;
                        padding: 8px;
                        margin-bottom: 10px;
                        font-weight: bold;
                    }
                    .chat-form-table {
                        width: 100%;
                        background-color: #c0c0c0;
                        border: 2px outset #ffffff;
                        padding: 8px;
                    }
                    .chat-textarea {
                        width: 98%;
                        font-family: "Courier New", monospace;
                        font-size: 13px;
                        resize: vertical;
                    }
                </style>
            </head>
            <body>
                <a href="#main-content" class="sr-only">Skip to main content</a>
                
                <div class="banner-marquee" role="region" aria-label="Announcement Marquee">
                    <marquee behavior="scroll" direction="left">
                        *** TALK TO CYBER-STEVE 90S AI! ASK ABOUT DIAL-UP, WINDOWS 95, BEANIE BABIES, AND MORE! ***
                    </marquee>
                </div>
                
                <br>
                
                <div class="win98-window">
                    <div class="win98-titlebar">
                        <span>💬 CyberSpace 1999 - Cyber-Steve AI Chat</span>
                        <div class="win98-controls" aria-hidden="true">
                            <span>_</span>
                            <span>&#9633;</span>
                            <span>X</span>
                        </div>
                    </div>
                    
                    <table class="main-layout">
                        <tr>
                            """ + sidebar + """
                            <!-- Main Content Area -->
                            <td class="content" id="main-content" tabindex="-1">
                                <main>
                                    <h1 class="flaming-header">Cyber-Steve 90s AI Chat</h1>
                                    
                                    <p>
                                        Step back in time to 1999! Chat with <strong>Cyber-Steve</strong>, your AI portal to 90s internet nostalgia.
                                    </p>
                                    
                                    <div id="errorBanner" role="alert" style="display: none;"></div>
                                    
                                    <div id="chatHistory" role="log" aria-live="polite" aria-label="Conversation History" tabindex="0">
                                        <div class="chat-bubble assistant-bubble">
                                            <div class="bubble-meta">
                                                <strong>Cyber-Steve</strong> <span class="bubble-time">[System]</span>
                                            </div>
                                            <div class="bubble-content">Greetings Cyber Traveler! I am Cyber-Steve, your 1990s AI assistant! Ask me about Dial-up internet, Windows 95, Beanie Babies, or anything retro!</div>
                                        </div>
                                    </div>
                                    
                                    <div id="typingIndicator" role="status" aria-live="polite" style="display: none;">
                                        Cyber-Steve is typing... ⏳
                                    </div>
                                    
                                    <form id="chatForm" onsubmit="return false;">
                                        <table class="chat-form-table">
                                            <tr>
                                                <td>
                                                    <label for="chatInput"><strong>Your Message:</strong></label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <textarea id="chatInput" class="chat-textarea" rows="3" placeholder="Type your 90s question here... (Press Enter to send, Shift+Enter for new line)" aria-required="true"></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding-top: 6px;">
                                                    <button type="submit" id="sendBtn" class="submit-btn">Send Message</button>
                                                    <button type="button" id="clearBtn" class="submit-btn" style="margin-left: 8px;">Clear Chat</button>
                                                </td>
                                            </tr>
                                        </table>
                                    </form>
                                </main>
                            </td>
                        </tr>
                    </table>
                </div>
                
                <script>
                document.addEventListener("DOMContentLoaded", function() {
                    var chatForm = document.getElementById("chatForm");
                    var chatInput = document.getElementById("chatInput");
                    var sendBtn = document.getElementById("sendBtn");
                    var clearBtn = document.getElementById("clearBtn");
                    var chatHistory = document.getElementById("chatHistory");
                    var typingIndicator = document.getElementById("typingIndicator");
                    var errorBanner = document.getElementById("errorBanner");
                    var isLoading = false;

                    function formatTime(timestamp) {
                        var date = timestamp ? new Date(timestamp) : new Date();
                        return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
                    }

                    function escapeHtml(str) {
                        if (!str) return "";
                        return str.replace(/&/g, '&amp;')
                                  .replace(/</g, '&lt;')
                                  .replace(/>/g, '&gt;')
                                  .replace(/"/g, '&quot;');
                    }

                    function formatMarkdown(text) {
                        if (!text) return "";
                        var lines = text.split("\\n");
                        var formattedLines = [];
                        var inList = false;

                        for (var i = 0; i < lines.length; i++) {
                            var line = lines[i];
                            var processedLine = escapeHtml(line);

                            // **bold**
                            processedLine = processedLine.replace(/\\*\\*(.*?)\\*\\*/g, '<strong>$1</strong>');

                            // `code`
                            processedLine = processedLine.replace(/`(.*?)`/g, '<code>$1</code>');

                            // - list item
                            var trimmed = line.trim();
                            if (trimmed.indexOf("- ") === 0) {
                                var itemContent = processedLine.replace(/^(\\s*)-\\s+/, '');
                                if (!inList) {
                                    inList = true;
                                    formattedLines.push('<ul style="margin: 4px 0; padding-left: 20px;">');
                                }
                                formattedLines.push('<li>' + itemContent + '</li>');
                            } else {
                                if (inList) {
                                    inList = false;
                                    formattedLines.push('</ul>');
                                }
                                formattedLines.push(processedLine);
                            }
                        }
                        if (inList) {
                            formattedLines.push('</ul>');
                        }

                        return formattedLines.join('<br>');
                    }

                    function appendMessage(sender, text, timestamp, isAssistant) {
                        var bubble = document.createElement("div");
                        bubble.className = "chat-bubble " + (isAssistant ? "assistant-bubble" : "user-bubble");

                        var meta = document.createElement("div");
                        meta.className = "bubble-meta";
                        meta.innerHTML = '<strong>' + escapeHtml(sender) + '</strong> <span class="bubble-time">[' + formatTime(timestamp) + ']</span>';

                        var content = document.createElement("div");
                        content.className = "bubble-content";
                        if (isAssistant) {
                            content.innerHTML = formatMarkdown(text);
                        } else {
                            content.textContent = text;
                        }

                        bubble.appendChild(meta);
                        bubble.appendChild(content);
                        chatHistory.appendChild(bubble);

                        chatHistory.scrollTop = chatHistory.scrollHeight;
                    }

                    function showError(msg) {
                        if (msg) {
                            errorBanner.textContent = msg;
                            errorBanner.style.display = "block";
                        } else {
                            errorBanner.textContent = "";
                            errorBanner.style.display = "none";
                        }
                    }

                    function setLoading(loading) {
                        isLoading = loading;
                        chatInput.disabled = loading;
                        sendBtn.disabled = loading;
                        clearBtn.disabled = loading;
                        typingIndicator.style.display = loading ? "block" : "none";
                    }

                    function sendMessage() {
                        var message = chatInput.value.trim();
                        if (!message || isLoading) return;

                        showError(null);
                        appendMessage("You", message, Date.now(), false);
                        chatInput.value = "";
                        setLoading(true);

                        fetch("/api/chat", {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json"
                            },
                            body: JSON.stringify({ message: message })
                        })
                        .then(function(res) {
                            if (!res.ok) {
                                throw new Error("HTTP error " + res.status);
                            }
                            return res.json();
                        })
                        .then(function(data) {
                            if (data.success) {
                                appendMessage("Cyber-Steve", data.reply, data.timestamp, true);
                            } else {
                                var errorMsg = data.error || "An error occurred while talking to Cyber-Steve.";
                                showError(errorMsg);
                                appendMessage("Cyber-Steve", "⚠️ Error: " + errorMsg, data.timestamp, true);
                            }
                        })
                        .catch(function(err) {
                            showError("Network or server error: " + err.message);
                        })
                        .finally(function() {
                            setLoading(false);
                            chatInput.focus();
                        });
                    }

                    if (chatForm) {
                        chatForm.addEventListener("submit", function(e) {
                            e.preventDefault();
                            sendMessage();
                        });
                    }

                    if (chatInput) {
                        chatInput.addEventListener("keydown", function(e) {
                            if (e.key === "Enter" && !e.shiftKey) {
                                e.preventDefault();
                                sendMessage();
                            }
                        });
                    }

                    if (clearBtn) {
                        clearBtn.addEventListener("click", function() {
                            chatHistory.innerHTML = "";
                            showError(null);
                            appendMessage("Cyber-Steve", "Greetings Cyber Traveler! I am Cyber-Steve, your 1990s AI assistant! Ask me about Dial-up internet, Windows 95, Beanie Babies, or anything retro!", Date.now(), true);
                        });
                    }
                });
                </script>
            </body>
            </html>
            """;

        byte[] responseBytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
