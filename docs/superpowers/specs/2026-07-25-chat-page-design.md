# Design Specification: Retro 90s Chat Page & MCP Integration

## 1. Overview
This document specifies the technical design for a new **Chat** page in the `demo-website` Java 21 module. The Chat page allows users to interact in real-time with **Cyber-Steve**, an AI assistant powered by the dedicated `retro90s-mcp` knowledge server.

---

## 2. Architecture & Component Diagram

```
+-------------------------------------------------------------------------+
|                              Browser UI                                 |
|   HTML5 + Vanilla JS + Win98 CSS + ARIA Live Regions + Keyboard Nav    |
+------------------------------------+------------------------------------+
                                     |
                                     | POST /api/chat (JSON payload)
                                     v
+-------------------------------------------------------------------------+
|                           demo-website Module                           |
|                                                                         |
|  +---------------------+      +---------------------------------------+  |
|  |     ChatHandler     | ---> |           Retro90sMcpClient           |  |
|  |  GET /chat          |      |  (Java 21 HttpClient -> JSON-RPC 2.0) |  |
|  |  POST /api/chat     |      +-------------------+-------------------+  |
|  +---------------------+                          |                      |
+---------------------------------------------------|---------------------+
                                                    |
                                                    | POST /message
                                                    v
+-------------------------------------------------------------------------+
|                          retro90s-mcp Module                            |
|             Retro90sServer (HTTP JSON-RPC 2.0 over /message)            |
|             ToolRegistry -> ask90s / explain / recommend                |
+-------------------------------------------------------------------------+
```

---

## 3. Detailed Component Specification

### 3.1 `Retro90sMcpClient.java` (`com.demo.website.mcp`)
* **Responsibility**: Manages JSON-RPC 2.0 requests over HTTP to the `retro90s-mcp` server.
* **Protocol**: Sends `POST /message` requests with `tools/call` method invoking the `ask90s` tool (with fallbacks to `explain` or `recommend`).
* **Session Context**: Formats multi-turn chat messages into structured prompt context.
* **Error Handling**: Catches network IO exceptions, timeouts, and JSON-RPC error frames gracefully, returning structured `McpChatResponse` domain objects with error flags.

### 3.2 `ChatHandler.java` (`com.demo.website.handlers`)
* **Responsibility**:
  * `GET /chat`: Renders the retro Win98 HTML chat interface.
  * `POST /api/chat`: Accepts client JSON request `{ "message": "...", "sessionId": "..." }`, invokes `Retro90sMcpClient`, and returns JSON response `{ "success": true, "reply": "...", "timestamp": "..." }`.
* **Security & Validation**: Input sanitization against XSS/HTML injection before rendering or forwarding.

### 3.3 `NavigationHelper.java` (`com.demo.website.handlers`)
* **Responsibility**: Updated to include `/chat` in sidebar navigation across all pages:
  ```html
  <a href="/chat" class="nav-btn">Cyber Chat 90s</a>
  ```

### 3.4 `DemoWebServer.java` (`com.demo.website`)
* Registers `/chat` and `/api/chat` contexts on `HttpServer`.

---

## 4. Chat UI & Retro 90s Styling

* **Container**: Standard `.win98-window` layout with titlebar: `💬 CyberSpace 1999 - Cyber-Steve AI Chat`.
* **Header**: Flaming header (`<h1 class="flaming-header">`), scrolling marquee banner, and description.
* **Chat History Box (`#chat-history`)**:
  * `role="log" aria-live="polite"` for screen readers.
  * Distinct message bubbles:
    * User message: Silver box with cyan label.
    * Cyber-Steve message: Terminal green-on-black (`#000000` / `#00ff00`) retro box with dial-up ASCII signature.
  * Preserves line breaks (`white-space: pre-wrap`) and formats retro markdown (`**bold**`, `code`, bullet points).
  * Includes message timestamps.
* **Input Area**:
  * Multiline `<textarea id="chat-input" rows="3">`.
  * Keyboard handling: `Enter` to send, `Shift + Enter` for newlines.
  * `<button id="send-btn" class="submit-btn">Send Transmission 🚀</button>`.
* **Loading & Error Indicators**:
  * Animated retro typing indicator when waiting for response.
  * Retro alert banner for connection or MCP failures without raw stack traces.

---

## 5. Accessibility (WCAG 2.1)
* ARIA roles (`role="log"`, `role="status"`).
* High-contrast focus rings (`outline: 3px solid #00ffff`).
* Skip link to main content (`<a href="#main-content" class="sr-only">`).
* Full keyboard navigation (no mouse required).

---

## 6. Code Quality & Ponytail Principles
* Pure JDK 21 `com.sun.net.httpserver` and `HttpClient`.
* Zero third-party framework overhead.
* DRY, SOLID, clean architecture.
