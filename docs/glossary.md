# Project Terminology & Technical Glossary

This glossary defines technical terms, project-specific concepts, protocol acronyms, and architectural concepts used across the repository and documentation.

---

## 🔤 Glossary Index

### A

* **A11y (Accessibility):** The design of software products to be usable by people with disabilities. In this project, evaluated against W3C WCAG guidelines.
* **AboutHandler (`com.demo.website.handlers.AboutHandler`):** Java HTTP handler serving the `/about` route in `demo-website`.
* **AccessibilityTestRunner (`com.demo.testing.AccessibilityTestRunner`):** Test runner that audits DOM elements for WCAG accessibility defects.

### B

* **BrowserContext:** A Playwright abstraction representing an isolated browser session (similar to an incognito profile).
* **Broken Link:** An anchor link (`<a>`) whose target URL returns an HTTP error status code (`≥ 400`) or cannot be reached.

### C

* **CDP (Chrome DevTools Protocol):** Low-level debugging protocol used by Playwright to communicate with headless Chromium instances.
* **Chromium:** The open-source browser engine used by Microsoft Playwright for headless rendering and testing.
* **ContactHandler (`com.demo.website.handlers.ContactHandler`):** Java HTTP handler managing GET requests for the guestbook form and POST submissions at `/contact`.

### D

* **`demo-website`:** Maven module serving a retro late-1990s website pre-seeded with intentional UX and accessibility defects.
* **`DemoWebServer` (`com.demo.website.DemoWebServer`):** Core web server class utilizing `com.sun.net.httpserver.HttpServer` and Java 21 virtual threads.

### F

* **Fat JAR / Uber JAR:** An executable JAR file containing both compiled project classes and all bundled third-party dependencies (`jar-with-dependencies`).
* **First Contentful Paint (FCP):** Performance metric measuring the time from page request until any part of the page content is rendered on screen.
* **FunctionalTestRunner (`com.demo.testing.FunctionalTestRunner`):** Test runner validating route availability, form POST submissions, broken links, and broken images.

### G

* **Golden Web Award:** A retro fake award badge rendered on `demo-website`'s home page for 1990s visual aesthetic.
* **Graphify:** Knowledge graph generation tool used to build semantic and AST relationships for repository components.

### H

* **Heading Hierarchy Skip (`HEADING_HIERARCHY_SKIP`):** An accessibility defect where heading levels jump non-sequentially (e.g. `<h1>` directly to `<h4>`), breaking screen reader navigation.
* **HomeHandler (`com.demo.website.handlers.HomeHandler`):** Java HTTP handler serving the `/` home route.
* **`HttpServer` (`com.sun.net.httpserver.HttpServer`):** Embedded HTTP server implementation provided by the JDK standard library.

### J

* **JSON-RPC 2.0:** A lightweight, stateless remote procedure call (RPC) protocol using JSON payloads for request, response, and error objects.
* **`JsonParser` (`com.demo.mcp.json.JsonParser`):** Custom recursive-descent JSON parser with zero third-party dependencies.
* **`JsonValue` (`com.demo.mcp.json.JsonValue`):** Dynamic value representation for JSON primitives, objects, and arrays.

### M

* **`MainTestPipeline` (`com.demo.testing.MainTestPipeline`):** Master entry point orchestrating server launch, test suite execution, report generation, and shutdown.
* **MCP (Model Context Protocol):** An open standard protocol created by Anthropic enabling AI models and client applications to interact with local tools, resources, and browser engines.
* **`mcp-server`:** Maven module implementing an MCP server over Standard I/O using Playwright Java.
* **`McpServer` (`com.demo.mcp.protocol.McpServer`):** Main class managing the Stdio event loop for JSON-RPC 2.0 messages.
* **Missing Alt Text (`MISSING_ALT_TEXT`):** Accessibility defect where an `<img>` tag lacks an `alt` description attribute.
* **Missing Form Label (`MISSING_FORM_LABEL`):** Accessibility defect where a form `<input>` element lacks an associated `<label for="...">` or `aria-label`.

### N

* **NavigationHelper (`com.demo.website.handlers.NavigationHelper`):** Helper class generating HTML sidebar navigation and layout markup.

### P

* **PerformanceTestRunner (`com.demo.testing.PerformanceTestRunner`):** Test runner collecting browser timing metrics via `window.performance`.
* **Playwright:** Open-source browser automation library created by Microsoft.
* **`PlaywrightManager` (`com.demo.mcp.tools.PlaywrightManager`):** Central manager in `mcp-server` encapsulating Playwright browser lifecycle and DOM operations.
* **`PlaywrightToolRegistry` (`com.demo.mcp.tools.PlaywrightToolRegistry`):** Registry class declaring tool definitions and executing tool calls.
* **Poor Color Contrast (`POOR_COLOR_CONTRAST`):** Accessibility flaw where text foreground and background color contrast ratio fails WCAG AA minimum 4.5:1 threshold.

### R

* **`reports/`:** Root directory storing generated Markdown test reports and visual screenshot artifacts.

### S

* **Small Click Target (`SMALL_CLICK_TARGET`):** UX defect where interactive link/button dimensions fall below 24x24 pixels.
* **StaticAssetHandler (`com.demo.website.handlers.StaticAssetHandler`):** Java HTTP handler serving static CSS and SVG assets from classpath `/static/`.
* **Stdio (Standard Input / Output):** Inter-process communication stream (`stdin` / `stdout`) used as the primary transport protocol for MCP servers.

### T

* **`testing-scenarios`:** Maven module containing the automated testing suite and report generators.
* **`TestReportGenerator` (`com.demo.testing.TestReportGenerator`):** Class responsible for compiling test results into structured Markdown reports.

### U

* **UxJourneyTestRunner (`com.demo.testing.UxJourneyTestRunner`):** Test runner simulating a multi-step user navigation path and capturing a visual proof screenshot.

### V

* **Virtual Threads:** Lightweight threads introduced in Java 21 (`JEP 444`) that significantly reduce memory overhead and context switching cost for high-concurrency I/O operations.

### W

* **WCAG (Web Content Accessibility Guidelines):** Internationally recognized standards for web accessibility published by W3C.
