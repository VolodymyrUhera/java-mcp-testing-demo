# API Specification: HTTP Endpoints & MCP Tools

This document provides a comprehensive API reference for both the **HTTP Web Server (`demo-website`)** endpoints and the **Model Context Protocol Stdio Tools (`mcp-server`)**.

---

## 🌐 1. HTTP Web Server API (`demo-website`)

The web server listens on `http://localhost:8080` by default.

### Endpoint Overview

| Method | Endpoint | Description | Content-Type |
|--------|----------|-------------|--------------|
| `GET` | `/` | Home page featuring Netscape banner, Golden Web Award, and retro styling. | `text/html; charset=UTF-8` |
| `GET` | `/about` | About page detailing site history and intentional WCAG contrast/text defects. | `text/html; charset=UTF-8` |
| `GET` | `/contact` | Contact guestbook form page. | `text/html; charset=UTF-8` |
| `POST` | `/contact` | Processes form submission (`username`, `email`, `comments`) and returns confirmation. | `text/html; charset=UTF-8` |
| `GET` | `/links` | Web links directory containing intentional confusing link labels. | `text/html; charset=UTF-8` |
| `GET` | `/static/*` | Static file server (`/static/style.css`, `/static/images/under_construction.svg`). | `text/css` / `image/svg+xml` |

---

### Endpoint Details

#### 1. `GET /`
* **Query Parameters:** None
* **Success Response:** `200 OK`
* **Body:** HTML document containing sidebar navigation, marquee banner, and home content.

#### 2. `GET /about`
* **Query Parameters:** None
* **Success Response:** `200 OK`
* **Body:** HTML document containing site about info and intentionally low-contrast text elements.

#### 3. `GET /contact`
* **Query Parameters:** None
* **Success Response:** `200 OK`
* **Body:** HTML form with fields `username`, `email`, `comments`, and submit button `#submitBtn`.

#### 4. `POST /contact`
* **Request Body:** Form URL Encoded (`application/x-www-form-urlencoded` or standard POST body)
  * `username` (string): User name.
  * `email` (string): User email address.
  * `comments` (string): Guestbook message.
* **Success Response:** `200 OK`
* **Body:** HTML confirmation containing message `"Electronic Mail Sent!"`.

#### 5. `GET /links`
* **Query Parameters:** None
* **Success Response:** `200 OK`
* **Body:** HTML document with links.

#### 6. `GET /static/{filename}`
* **Path Parameters:** `filename` (e.g. `style.css`, `images/under_construction.svg`).
* **Success Response:** `200 OK` with appropriate `Content-Type` header.
* **Error Response:** `404 Not Found` if resource does not exist in classpath `/static/`.

---

## 🤖 2. MCP JSON-RPC 2.0 Stdio Tools (`mcp-server`)

The MCP Server listens on standard input (`stdin`) for newline-terminated JSON-RPC 2.0 payloads and outputs JSON-RPC 2.0 responses to standard output (`stdout`).

### Protocol Initialization

#### `initialize` Method

**Request:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {}
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "protocolVersion": "2024-11-05",
    "capabilities": {
      "tools": {
        "listChanged": false
      }
    },
    "serverInfo": {
      "name": "java-playwright-mcp-server",
      "version": "1.0.0"
    }
  }
}
```

---

### Standard Protocol Methods

* **`tools/list`:** Returns JSON array of all 12 tool schemas.
* **`tools/call`:** Invokes a tool by name with arguments.

---

### Tool Catalog (12 Tools)

#### 1. `launch_browser`
* **Description:** Launches Playwright Chromium browser instance.
* **Parameters:**
  * `headless` (boolean, optional): Run browser in headless mode (`true`/`false`).
* **Example Tool Call:**
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/call",
  "params": {
    "name": "launch_browser",
    "arguments": { "headless": true }
  }
}
```
* **Response Output:** `"Browser launched successfully."`

---

#### 2. `close_browser`
* **Description:** Closes active Playwright browser instance and releases system resources.
* **Parameters:** None
* **Response Output:** `"Browser closed successfully."`

---

#### 3. `open_url` / `navigate`
* **Description:** Navigates active browser page to target URL.
* **Parameters:**
  * `url` (string, required): Target web page URL.
* **Example:**
```json
{
  "jsonrpc": "2.0",
  "id": 3,
  "method": "tools/call",
  "params": {
    "name": "open_url",
    "arguments": { "url": "http://localhost:8080" }
  }
}
```
* **Response Output:** `"Navigated to http://localhost:8080 [Status: 200]"`

---

#### 4. `click`
* **Description:** Clicks element matching specified CSS selector.
* **Parameters:**
  * `selector` (string, required): CSS selector.
* **Example:**
```json
{
  "jsonrpc": "2.0",
  "id": 4,
  "method": "tools/call",
  "params": {
    "name": "click",
    "arguments": { "selector": "a[href='/about']" }
  }
}
```
* **Response Output:** `"Clicked element matching selector: a[href='/about']"`

---

#### 5. `fill_form`
* **Description:** Fills input element matching selector with value.
* **Parameters:**
  * `selector` (string, required): CSS selector.
  * `value` (string, required): Text input value.
* **Example:**
```json
{
  "jsonrpc": "2.0",
  "id": 5,
  "method": "tools/call",
  "params": {
    "name": "fill_form",
    "arguments": {
      "selector": "input[name='username']",
      "value": "QA_User"
    }
  }
}
```
* **Response Output:** `"Filled selector 'input[name='username']' with value 'QA_User'"`

---

#### 6. `wait_for_selector`
* **Description:** Waits for element selector to appear in DOM.
* **Parameters:**
  * `selector` (string, required): CSS selector.
  * `timeout` (number, optional): Timeout in milliseconds (default: `5000.0`).
* **Response Output:** `"Element matching '#contactForm' is ready."`

---

#### 7. `evaluate_javascript`
* **Description:** Executes custom JavaScript snippet on current browser page.
* **Parameters:**
  * `script` (string, required): JavaScript snippet to evaluate.
* **Example:**
```json
{
  "jsonrpc": "2.0",
  "id": 6,
  "method": "tools/call",
  "params": {
    "name": "evaluate_javascript",
    "arguments": { "script": "document.title" }
  }
}
```
* **Response Output:** `"Evaluation Result: Welcome to CyberSpace 1999 Ultimate Web Home!"`

---

#### 8. `take_screenshot`
* **Description:** Captures full-page screenshot and saves to file.
* **Parameters:**
  * `filePath` (string, optional): Output image path (default: `reports/screenshot.png`).
* **Response Output:** `"Screenshot saved to reports/screenshot.png"`

---

#### 9. `save_pdf`
* **Description:** Saves current page as a PDF file.
* **Parameters:**
  * `filePath` (string, optional): Output PDF path (default: `reports/page.pdf`).
* **Response Output:** `"PDF saved to reports/page.pdf"`

---

#### 10. `extract_content`
* **Description:** Extracts page title, current URL, body text, and all anchor link elements (`text`, `href`).
* **Parameters:** None
* **Response Output:** `"Extracted Content: {title=..., url=..., links=[...], bodyText=...}"`

---

#### 11. `get_performance_metrics`
* **Description:** Collects page performance timing metrics (`loadTimeMs`, `domContentLoadedMs`, `firstContentfulPaintMs`, `resourceCount`).
* **Parameters:** None
* **Response Output:** `"Performance Metrics: {loadTimeMs=120, domContentLoadedMs=45, firstContentfulPaintMs=60, resourceCount=3}"`

---

#### 12. `analyze_accessibility`
* **Description:** Runs DOM accessibility audit checking images for `alt` attributes, form fields for associated `<label>` tags, heading hierarchy jumps, and contrast indicators.
* **Parameters:** None
* **Response Output:** `"Accessibility Analysis: {totalImages=2, missingAltCount=2, totalFormInputs=3, unlabelledInputCount=3, headingJumps=1, headingHierarchy=[1, 4, 4]}"`
