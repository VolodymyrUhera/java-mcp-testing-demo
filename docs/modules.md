# Module & Package Documentation

This document provides a detailed technical analysis of the three Maven modules in the project repository, their underlying packages, classes, and responsibilities.

---

## 🏛️ Maven Project Structure

The project is structured as a Maven multi-module project governed by the parent POM (`pom.xml`).

| Module Name | Artifact ID | Packaging | Description |
|-------------|-------------|-----------|-------------|
| `demo-website` | `demo-website` | `jar` | Standalone retro late-1990s HTTP web server with intentional UX/A11y defects. |
| `mcp-server` | `mcp-server` | `jar` (Assembly `jar-with-dependencies`) | Java Playwright Model Context Protocol (MCP) server over Stdio. |
| `testing-scenarios` | `testing-scenarios` | `jar` (Assembly `jar-with-dependencies`) | Automated functional, performance, accessibility, and UX journey testing pipeline. |

---

## 🌐 1. Module: `demo-website`

**Artifact:** `com.demo:demo-website:1.0.0-SNAPSHOT`  
**Main Class:** `com.demo.website.DemoWebServer`  
**Dependencies:** None (Uses Java 21 Standard Library only).

### Package `com.demo.website`

#### `DemoWebServer` ([DemoWebServer.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/DemoWebServer.java))
* **Responsibility:** Bootstraps and manages the embedded `com.sun.net.httpserver.HttpServer` instance.
* **Key Configuration:**
  * Default Port: `8080` (overridable via command-line argument `args[0]`).
  * Executor: `Executors.newVirtualThreadPerTaskExecutor()` (Java 21 Virtual Threads).
* **Key Methods:**
  * `DemoWebServer(int port)`: Initializes server context mappings (`/`, `/about`, `/contact`, `/links`, `/static`).
  * `start()`: Starts listening for HTTP requests.
  * `stop()`: Gracefully terminates the HTTP server (`server.stop(0)`).
  * `main(String[] args)`: Entry point for running the web server standalone.

### Package `com.demo.website.handlers`

Contains HTTP context handlers implementing `com.sun.net.httpserver.HttpHandler`:

#### `HomeHandler` ([HomeHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/HomeHandler.java))
* **Route:** `GET /`
* **Content:** Renders home page featuring a Netscape marquee, Golden Web award badge, under-construction banner, and intentional defects (`HEADING_HIERARCHY_SKIP` from `<h1>` to `<h4>`, `SMALL_CLICK_TARGET` link).

#### `AboutHandler` ([AboutHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/AboutHandler.java))
* **Route:** `GET /about`
* **Content:** Renders "About Us" page with intentional defects (`MISSING_ALT_TEXT` on image, `POOR_COLOR_CONTRAST` text `#a0a0a0` on white, `UNFORMATTED_WALL_OF_TEXT`).

#### `ContactHandler` ([ContactHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/ContactHandler.java))
* **Route:** `GET /contact`, `POST /contact`
* **Content:** Renders contact guestbook form with intentional defects (`MISSING_FORM_LABEL` on username field, `SMALL_CLICK_TARGET` submit button). Handles POST submission and returns confirmation HTML `"Electronic Mail Sent!"`.

#### `LinksHandler` ([LinksHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/LinksHandler.java))
* **Route:** `GET /links`
* **Content:** Renders external web directory with intentional UX defects (`CONFUSING_LINK_LABELS` such as "Click Here").

#### `StaticAssetHandler` ([StaticAssetHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/StaticAssetHandler.java))
* **Route:** `GET /static/*`
* **Content:** Serves static resources (`style.css`, `under_construction.svg`) from the classpath (`/static/`). Sets appropriate `Content-Type` headers (`text/css`, `image/svg+xml`).

#### `NavigationHelper` ([NavigationHelper.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/NavigationHelper.java))
* **Responsibility:** Helper class providing HTML template fragments for the sidebar navigation, retro header, visitor counter, and CSS layout.

---

## 🤖 2. Module: `mcp-server`

**Artifact:** `com.demo:mcp-server:1.0.0-SNAPSHOT`  
**Main Class:** `com.demo.mcp.protocol.McpServer`  
**Dependencies:** `com.microsoft.playwright:playwright:1.49.0`.

### Package `com.demo.mcp.protocol`

#### `McpServer` ([McpServer.java](file:///home/voha/Documents/JiraMCP/mcp-server/src/main/java/com/demo/mcp/protocol/McpServer.java))
* **Responsibility:** Standard I/O (Stdio) event loop listening for JSON-RPC 2.0 requests on `System.in` and emitting responses to `System.out`.
* **Protocol Handlers:**
  * `initialize`: Negotiates protocol version (`2024-11-05`), capabilities, and server info (`java-playwright-mcp-server v1.0.0`).
  * `tools/list`: Returns list of 12 registered tool schemas from `PlaywrightToolRegistry`.
  * `tools/call`: Executes requested tool by name with arguments and formats result into standard MCP content block.
  * `notifications/initialized`: Silently acknowledges client initialization notification.

#### `McpMessage` ([McpMessage.java](file:///home/voha/Documents/JiraMCP/mcp-server/src/main/java/com/demo/mcp/protocol/McpMessage.java))
* **Responsibility:** Utility class for constructing JSON-RPC 2.0 success responses, tool execution result objects, and error responses (`-32601 Method Not Found`, `-32700 Parse Error`).

### Package `com.demo.mcp.json`

Zero-dependency JSON manipulation library built specifically for high-speed stdio payload processing:

#### `JsonParser` ([JsonParser.java](file:///home/voha/Documents/JiraMCP/mcp-server/src/main/java/com/demo/mcp/json/JsonParser.java))
* **Responsibility:** Recursive-descent parser parsing JSON text strings into `JsonValue` trees (objects, arrays, strings, numbers, booleans, nulls).

#### `JsonValue` ([JsonValue.java](file:///home/voha/Documents/JiraMCP/mcp-server/src/main/java/com/demo/mcp/json/JsonValue.java))
* **Responsibility:** Dynamic value type wrapping primitive types, lists, and maps. Provides accessor methods (`asString()`, `asBoolean()`, `asDouble()`, `get(key)`, `toJson()`).

### Package `com.demo.mcp.tools`

#### `PlaywrightToolRegistry` ([PlaywrightToolRegistry.java](file:///home/voha/Documents/JiraMCP/mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightToolRegistry.java))
* **Responsibility:** Registers and exposes tool definitions matching the MCP specification schema, and dispatches `executeTool(name, args)` calls to `PlaywrightManager`.
* **Exposed Tools (12):** `launch_browser`, `close_browser`, `open_url`, `navigate`, `click`, `fill_form`, `wait_for_selector`, `evaluate_javascript`, `take_screenshot`, `save_pdf`, `extract_content`, `get_performance_metrics`, `analyze_accessibility`.

#### `PlaywrightManager` ([PlaywrightManager.java](file:///home/voha/Documents/JiraMCP/mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightManager.java))
* **Responsibility:** Manages lifecycle of `com.microsoft.playwright.Playwright`, `Browser`, `BrowserContext`, and `Page` instances.
* **Key Automation Operations:**
  * `launchBrowser(boolean headless)`: Starts Playwright Chromium process.
  * `navigate(String url)`: Navigates active page to target URL.
  * `click(String selector)` / `fillForm(String selector, String value)`: Interacts with DOM elements.
  * `takeScreenshot(String path)` / `savePdf(String path)`: Captures page artifacts.
  * `getPerformanceMetrics()`: Executes browser `window.performance` JavaScript API script.
  * `analyzeAccessibility()`: Executes DOM audit script inspecting `<img>` alt tags, `<input>` labels, contrast classes, and heading hierarchy.

---

## 🧪 3. Module: `testing-scenarios`

**Artifact:** `com.demo:testing-scenarios:1.0.0-SNAPSHOT`  
**Main Class:** `com.demo.testing.MainTestPipeline`  
**Dependencies:** `demo-website`, `mcp-server`, `playwright`.

### Package `com.demo.testing`

#### `MainTestPipeline` ([MainTestPipeline.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/MainTestPipeline.java))
* **Responsibility:** Master controller orchestrating the full test execution lifecycle:
  1. Starts `DemoWebServer` on port 8080.
  2. Launches headless Playwright Chromium.
  3. Executes `FunctionalTestRunner`, `PerformanceTestRunner`, `AccessibilityTestRunner`, and `UxJourneyTestRunner`.
  4. Calls `TestReportGenerator` to emit reports.
  5. Shuts down browser and server gracefully.

#### `FunctionalTestRunner` ([FunctionalTestRunner.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/FunctionalTestRunner.java))
* **Responsibility:** Validates route status codes (via `java.net.http.HttpClient`), scans for broken links (`<a>`), verifies image rendering (`<img>` `naturalWidth > 0`), and submits the contact form.

#### `PerformanceTestRunner` ([PerformanceTestRunner.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/PerformanceTestRunner.java))
* **Responsibility:** Navigates through all site routes and evaluates `performance.getEntriesByType('navigation')` and `first-contentful-paint` to measure `loadTimeMs`, `domContentLoadedMs`, `firstContentfulPaintMs`, and resource counts.

#### `AccessibilityTestRunner` ([AccessibilityTestRunner.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/AccessibilityTestRunner.java))
* **Responsibility:** Runs DOM analysis script to identify WCAG accessibility flaws (`MISSING_ALT_TEXT`, `MISSING_FORM_LABEL`, `HEADING_HIERARCHY_SKIP`, `POOR_COLOR_CONTRAST`, `SMALL_CLICK_TARGET`).

#### `UxJourneyTestRunner` ([UxJourneyTestRunner.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/UxJourneyTestRunner.java))
* **Responsibility:** Simulates realistic user multi-step navigation (Home -> About -> Home -> Contact -> Fill Form -> Submit -> Screenshot), measuring latency per step and outputting proof image `reports/ux_journey_screenshot.png`.

#### `TestReportGenerator` ([TestReportGenerator.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/TestReportGenerator.java))
* **Responsibility:** Aggregates test run statistics and generates structured Markdown reports in `reports/` directory (`functional_report.md`, `performance_report.md`, `accessibility_report.md`, `ux_journey_report.md`).
