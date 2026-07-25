# Retro 90s Chat Page & MCP Integration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a new responsive, accessible 90s retro Chat page in `demo-website` integrating with the `retro90s-mcp` knowledge server over HTTP JSON-RPC 2.0.

**Architecture:** Create `Retro90sMcpClient` in `com.demo.website.mcp` using Java 21 `HttpClient` for JSON-RPC `tools/call` communication with `retro90s-mcp`. Implement `ChatHandler` in `com.demo.website.handlers` serving `/chat` (GET) and `/api/chat` (POST), update `NavigationHelper` to include `/chat`, and add interactive HTML/JS/CSS for Win98 chat UI with keyboard shortcuts (Enter to send, Shift+Enter for newline) and WCAG ARIA accessibility.

**Tech Stack:** Java 21 (`com.sun.net.httpserver`, `java.net.http.HttpClient`), Vanilla HTML5/CSS3/JavaScript (Win98 retro theme).

## Global Constraints

- Java 21 compatibility.
- Zero extra frameworks or external dependencies.
- Follow existing package structure in `demo-website`.
- Preserve retro 90s aesthetics and accessibility standards (WCAG).

---

### Task 1: NavigationHelper & Web Server Context Registration

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/NavigationHelper.java`
- Modify: `demo-website/src/main/java/com/demo/website/DemoWebServer.java`

**Interfaces:**
- Consumes: Existing HTTP Server routing.
- Produces: Sidebar HTML containing `/chat` link, and context handler registration for `/chat` and `/api/chat`.

- [ ] **Step 1: Update `NavigationHelper.java` to add the `/chat` navigation button**

In `demo-website/src/main/java/com/demo/website/handlers/NavigationHelper.java`:
Add boolean `isChat = "/chat".equals(activePath);`, compute `chatClass` and `chatAria`, and append the `<a href="/chat" class="...">Cyber Chat 90s</a>` link right after `Secret Portal` or `Cool Links`.

- [ ] **Step 2: Add placeholder `ChatHandler.java` stub and register in `DemoWebServer.java`**

Create `demo-website/src/main/java/com/demo/website/handlers/ChatHandler.java` stub implementing `HttpHandler`.
Register `server.createContext("/chat", chatHandler);` and `server.createContext("/api/chat", chatHandler);` in `DemoWebServer.java`.

- [ ] **Step 3: Verify compilation with Maven**

Run: `mvn test-compile`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit changes**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/NavigationHelper.java demo-website/src/main/java/com/demo/website/DemoWebServer.java demo-website/src/main/java/com/demo/website/handlers/ChatHandler.java
git commit -m "feat(website): add /chat navigation link and context routing"
```

---

### Task 2: Backend `Retro90sMcpClient` Service

**Files:**
- Create: `demo-website/src/main/java/com/demo/website/mcp/Retro90sMcpClient.java`
- Create: `demo-website/src/main/java/com/demo/website/mcp/McpChatResponse.java`

**Interfaces:**
- Consumes: `retro90s-mcp` HTTP endpoint (`http://localhost:8080/message` or fallback).
- Produces: `McpChatResponse sendPrompt(String prompt)` returning assistant text response or structured error info.

- [ ] **Step 1: Write `McpChatResponse.java` model**

Create `demo-website/src/main/java/com/demo/website/mcp/McpChatResponse.java` with fields: `boolean success`, `String content`, `String error`, `long timestamp`.

- [ ] **Step 2: Write `Retro90sMcpClient.java`**

Implement `Retro90sMcpClient` with `HttpClient` communicating with `retro90s-mcp` using JSON-RPC 2.0 `tools/call` for `ask90s`. Parse JSON responses safely using lightweight Jackson or stdlib string/pattern parsing, handling timeouts and network errors without throwing unhandled exceptions.

- [ ] **Step 3: Verify compilation**

Run: `mvn test-compile`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit changes**

```bash
git add demo-website/src/main/java/com/demo/website/mcp/
git commit -m "feat(website): implement Retro90sMcpClient service for retro90s-mcp communication"
```

---

### Task 3: ChatHandler Implementation (`GET /chat` & `POST /api/chat`)

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/handlers/ChatHandler.java`

**Interfaces:**
- Consumes: `Retro90sMcpClient`
- Produces: HTML page output for `GET /chat`, JSON responses for `POST /api/chat`.

- [ ] **Step 1: Implement `handle` method in `ChatHandler.java`**

For `POST /api/chat`:
- Parse request body JSON `{ "message": "..." }`.
- Sanitize input against XSS.
- Call `Retro90sMcpClient.sendPrompt(message)`.
- Return HTTP 200 with JSON response `{ "success": true, "reply": "...", "timestamp": "..." }`.

For `GET /chat`:
- Build full retro HTML page containing Win98 chat UI, chat history container, multiline textarea, Send button, keyboard shortcuts, typing animation, and embedded client JavaScript.

- [ ] **Step 2: Verify compilation and local server run**

Run: `mvn test-compile`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit changes**

```bash
git add demo-website/src/main/java/com/demo/website/handlers/ChatHandler.java
git commit -m "feat(website): implement ChatHandler GET and POST API endpoints"
```

---

### Task 4: Retro CSS & Accessibility Enhancements

**Files:**
- Modify: `demo-website/src/main/resources/static/style.css`

**Interfaces:**
- Consumes: HTML structure from `ChatHandler`.
- Produces: CSS rules for `.chat-box`, `.chat-msg-user`, `.chat-msg-assistant`, `.typing-indicator`, `.error-banner`, high contrast focus rings.

- [ ] **Step 1: Update `style.css` with chat component styles**

Add Win98 retro styles for chat area:
- `.chat-history`: fixed height, scrollable, `border: 2px inset var(--win-white)`, `background: #ffffff;`.
- `.chat-msg-user`: silver background, cyan top border, right-aligned or left-aligned badge.
- `.chat-msg-assistant`: black background (`#000000`), green retro text (`#00ff00`), monospace font.
- `.typing-indicator`: yellow/green pulsing retro text.
- Focus rings for accessibility (`textarea:focus`, `button:focus`).

- [ ] **Step 2: Verify build**

Run: `mvn clean package -DskipTests`
Expected: BUILD SUCCESS

- [ ] **Step 3: Commit changes**

```bash
git add demo-website/src/main/resources/static/style.css
git commit -m "style(website): add retro 90s chat UI styling and high contrast accessibility rules"
```

---

### Task 5: End-to-End Verification & Verification Pass

**Files:**
- Modify/Verify: All `demo-website` files.

- [ ] **Step 1: Compile complete Maven reactor**

Run: `mvn clean package -DskipTests`
Expected: BUILD SUCCESS across all 5 modules.

- [ ] **Step 2: Run verification pass**

Verify that:
1. Chat page is fully implemented at `/chat`.
2. MCP integration works cleanly via `Retro90sMcpClient`.
3. Accessibility (ARIA live regions, focus rings, skip link, labels) is intact.
4. Responsive Win98 layout operates seamlessly.
5. No existing code or functionality was broken.

- [ ] **Step 3: Final Commit**

```bash
git add .
git commit -m "feat: complete retro 90s chat page with retro90s-mcp integration"
```
