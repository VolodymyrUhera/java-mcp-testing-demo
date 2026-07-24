# Java MCP Server, Demo Website & Automated Testing Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a complete demonstration project containing a Java Playwright MCP Server, a standalone late-1990s themed Java HTTP website with intentional UX/accessibility defects, and an automated testing suite that evaluates functional, performance, accessibility, and UX journey metrics while outputting JSON/Markdown reports and detailed documentation.

**Architecture:** A multi-module Maven project in Java 21. `demo-website` uses Java's built-in `com.sun.net.httpserver.HttpServer` to serve retro-styled HTML/CSS pages without external web frameworks. `mcp-server` implements an MCP protocol (JSON-RPC over stdio) wrapping Microsoft Playwright for Java to execute browser automation tools. `testing-scenarios` runs Playwright-based automated test pipelines against `demo-website` to collect accessibility, performance, and functional metrics and save reports to `reports/`.

**Tech Stack:** Java 21, Maven, `com.sun.net.httpserver.HttpServer`, Microsoft Playwright for Java (`com.microsoft.playwright:playwright:1.49.0`), Java Standard Library (`java.net.http`, `java.nio`, `java.util.logging`, Java Records).

## Global Constraints

- Use Java 21 features (Records, Sealed Types, Pattern Matching, Text Blocks).
- Use Maven only as the build system (`pom.xml`).
- Do NOT use any third-party web framework or JSON framework unless justified; prefer standard library. Playwright for Java (`com.microsoft.playwright:playwright`) is pre-approved for browser automation.
- All code must follow SOLID, DRY, KISS, and YAGNI principles.
- Every task must compile successfully, include error handling, and pass verification.

---

### Task 1: Project Scaffolding & Multi-Module Maven Setup

**Files:**
- Create: `pom.xml`
- Create: `demo-website/pom.xml`
- Create: `mcp-server/pom.xml`
- Create: `testing-scenarios/pom.xml`

**Interfaces:**
- Consumes: Java 21 SDK & Maven build environment
- Produces: Maven parent-child build configuration for all three modules.

- [ ] **Step 1: Create Parent pom.xml**

Write parent `pom.xml` defining Java 21 source/target compatibility, UTF-8 encoding, and module definitions for `demo-website`, `mcp-server`, and `testing-scenarios`.

- [ ] **Step 2: Create Module pom.xml Files**

Write child `pom.xml` for `demo-website` (pure stdlib), `mcp-server` (with `com.microsoft.playwright:playwright:1.49.0`), and `testing-scenarios` (with `mcp-server` / `playwright` dependencies).

- [ ] **Step 3: Create Directory Structure**

Create directory trees for each module (`src/main/java`, `src/test/java`) and top-level directories (`docs/`, `reports/`).

- [ ] **Step 4: Verify Maven Scaffolding**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS` across parent and all three child modules.

- [ ] **Step 5: Commit Scaffolding**

Run: `git add . && git commit -m "chore: scaffold multi-module maven project for java 21"`

---

### Task 2: Retro Late-1990s Demo Website (`demo-website`)

**Files:**
- Create: `demo-website/src/main/java/com/demo/website/DemoWebServer.java`
- Create: `demo-website/src/main/java/com/demo/website/handlers/HomeHandler.java`
- Create: `demo-website/src/main/java/com/demo/website/handlers/AboutHandler.java`
- Create: `demo-website/src/main/java/com/demo/website/handlers/ContactHandler.java`
- Create: `demo-website/src/main/java/com/demo/website/handlers/StaticAssetHandler.java`
- Create: `demo-website/src/main/resources/static/style.css`

**Interfaces:**
- Consumes: `com.sun.net.httpserver.HttpServer`
- Produces: HTTP Server running on `http://localhost:8080` serving Home, About, and Contact pages with intentional UX & Accessibility flaws.

- [ ] **Step 1: Create Static Stylesheet (`style.css`)**

Implement retro 1990s CSS: bright neon background colors, retro fonts (Comic Sans MS, Times New Roman), table borders, animated marquee styling, low contrast colors for testing intentional flaws.

- [ ] **Step 2: Create HTTP Server Entrypoint (`DemoWebServer.java`)**

Configure `HttpServer` listening on port 8080 with routes `/`, `/about`, `/contact`, and `/static/*`.

- [ ] **Step 3: Create Home Page Handler (`HomeHandler.java`)**

Serve Home page with retro layout: visitor counter, marquee text, under construction text banner, fake award badges, intentional missing ALT texts, and skipped heading hierarchy (H1 -> H4).

- [ ] **Step 4: Create About Us Handler (`AboutHandler.java`)**

Serve About page with retro content, long unformatted text blocks, low contrast text (`#888888` on `#999999`), and small click targets (8px font links).

- [ ] **Step 5: Create Contact Us Handler (`ContactHandler.java`)**

Serve Contact Us page supporting `GET` (form rendering with missing `<label>` tags and tiny submit button) and `POST` (form handler returning confirmation response).

- [ ] **Step 6: Build and Run Demo Server Test**

Compile `demo-website` and verify HTTP responses for `/`, `/about`, and `/contact` via `HttpClient`.

- [ ] **Step 7: Commit Demo Website**

Run: `git add demo-website/ && git commit -m "feat(demo-website): add retro 1990s java http server with intentional ux defects"`

---

### Task 3: Lightweight JSON Parser & Java Playwright MCP Server (`mcp-server`)

**Files:**
- Create: `mcp-server/src/main/java/com/demo/mcp/json/JsonValue.java`
- Create: `mcp-server/src/main/java/com/demo/mcp/json/JsonParser.java`
- Create: `mcp-server/src/main/java/com/demo/mcp/protocol/McpMessage.java`
- Create: `mcp-server/src/main/java/com/demo/mcp/protocol/McpServer.java`
- Create: `mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightToolRegistry.java`
- Create: `mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightManager.java`

**Interfaces:**
- Consumes: Playwright Java API (`com.microsoft.playwright.*`), Stdio input/output
- Produces: Executable MCP server process responding to JSON-RPC tool calls over stdio.

- [ ] **Step 1: Implement Dependency-Free JSON Utility (`JsonParser.java`, `JsonValue.java`)**

Implement minimal zero-dependency JSON builder and parser supporting JSON-RPC standard request/response handling.

- [ ] **Step 2: Implement Playwright Manager (`PlaywrightManager.java`)**

Manage Playwright lifecycle: launching browser (Chromium), creating browser contexts/pages, navigating, taking screenshots, saving PDFs, evaluating JS, extracting content, and closing browser.

- [ ] **Step 3: Implement Tool Registry (`PlaywrightToolRegistry.java`)**

Define and register the 12 required MCP tools:
1. `launch_browser`
2. `close_browser`
3. `open_url`
4. `navigate`
5. `click`
6. `fill_form`
7. `wait_for_selector`
8. `evaluate_javascript`
9. `take_screenshot`
10. `save_pdf`
11. `extract_content`
12. `get_performance_metrics`
13. `analyze_accessibility`

- [ ] **Step 4: Implement Stdio MCP Server Loop (`McpServer.java`)**

Listen for incoming JSON-RPC lines on `System.in`, process `initialize`, `tools/list`, and `tools/call`, sending JSON responses to `System.out`.

- [ ] **Step 5: Unit Test MCP Server JSON-RPC Protocol**

Write tests for JSON parser and tool registration to ensure standard format responses.

- [ ] **Step 6: Commit MCP Server**

Run: `git add mcp-server/ && git commit -m "feat(mcp-server): implement java playwright mcp server over stdio"`

---

### Task 4: Automated Testing Scenarios Suite (`testing-scenarios`)

**Files:**
- Create: `testing-scenarios/src/main/java/com/demo/testing/FunctionalTestRunner.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/PerformanceTestRunner.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/AccessibilityTestRunner.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/UxJourneyTestRunner.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/TestReportGenerator.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/MainTestPipeline.java`

**Interfaces:**
- Consumes: Running `demo-website` HTTP server at `http://localhost:8080`, Playwright Browser API
- Produces: Automated test executions, metric gathering, and generation of reports in `reports/`.

- [ ] **Step 1: Implement Functional Test Runner (`FunctionalTestRunner.java`)**

Validate page status codes (200 OK), navigation flow, link resolution, contact form submission, and response validation.

- [ ] **Step 2: Implement Performance Test Runner (`PerformanceTestRunner.java`)**

Collect load time, DOMContentLoaded duration, First/Largest Contentful Paint using PerformanceTiming / PerformanceObserver, resource count, and identify slow resources.

- [ ] **Step 3: Implement Accessibility Test Runner (`AccessibilityTestRunner.java`)**

Scan DOM for missing `alt` attributes, unlabelled form inputs, skipped heading levels (H1 to H4), contrast ratios, keyboard focusability, and tab order.

- [ ] **Step 4: Implement UX Journey Test Runner (`UxJourneyTestRunner.java`)**

Execute sequence: Home -> About -> Home -> Contact -> Fill Form -> Submit -> Capture Screenshot. Record total duration, interaction count, defect counters, and capture step screenshot.

- [ ] **Step 5: Implement Test Report Generator (`TestReportGenerator.java`)**

Format gathered metrics into structured JSON files (`reports/test_results.json`, `reports/ux_journey_report.json`) and clean Markdown files (`reports/functional_report.md`, `reports/performance_report.md`, `reports/accessibility_report.md`, `reports/ux_journey_report.md`).

- [ ] **Step 6: Create Main Execution Pipeline (`MainTestPipeline.java`)**

Orchestrate starting `DemoWebServer`, executing all four test runners, writing report files, and shutting down server gracefully.

- [ ] **Step 7: Commit Testing Scenarios**

Run: `git add testing-scenarios/ && git commit -m "feat(testing-scenarios): implement functional, performance, a11y, and ux journey test runners"`

---

### Task 5: End-to-End Pipeline Execution & Verification

**Files:**
- Output: `reports/*.json`
- Output: `reports/*.md`
- Output: `reports/ux_journey_screenshot.png`

**Interfaces:**
- Consumes: Executable binaries of demo-website and testing-scenarios
- Produces: Generated report files in `reports/` folder.

- [ ] **Step 1: Build Full Project via Maven**

Run: `mvn clean package`
Expected: Successful compilation and packaging of all modules.

- [ ] **Step 2: Execute Automated Test Pipeline**

Run: `java -cp testing-scenarios/target/testing-scenarios-1.0.0-SNAPSHOT-jar-with-dependencies.jar:demo-website/target/demo-website-1.0.0-SNAPSHOT.jar com.demo.testing.MainTestPipeline`
Expected: Automated server startup, test suite execution, report generation, and clean shutdown.

- [ ] **Step 3: Verify Reports Generated in `reports/`**

Check that `reports/functional_report.md`, `reports/performance_report.md`, `reports/accessibility_report.md`, `reports/ux_journey_report.md`, and `reports/ux_journey_screenshot.png` exist and are populated.

- [ ] **Step 4: Commit Pipeline Verification**

Run: `git add reports/ && git commit -m "test: execute e2e test pipeline and generate test reports"`

---

### Task 6: Documentation & Architecture Diagrams (`docs/` & `README.md`)

**Files:**
- Create: `README.md`
- Create: `docs/architecture.md`
- Create: `docs/mcp_examples.md`
- Create: `docs/intentional_defects.md`

**Interfaces:**
- Consumes: Completed project codebase and test results
- Produces: Technical specifications, build/run instructions, Mermaid diagrams, MCP JSON-RPC examples.

- [ ] **Step 1: Create Architecture Documentation (`docs/architecture.md`)**

Write detailed documentation with Mermaid diagrams: Component Diagram, Sequence Diagram (MCP tool call & UX Journey), and Deployment Diagram.

- [ ] **Step 2: Create MCP Tool Usage Examples (`docs/mcp_examples.md`)**

Provide JSON-RPC request and response payloads for each of the 12 MCP tools exposed by `mcp-server`.

- [ ] **Step 3: Document Intentional UX/A11y Defects (`docs/intentional_defects.md`)**

Document every intentional flaw built into `demo-website` with reason, impact, and standard remedy.

- [ ] **Step 4: Create Root `README.md`**

Write clear guide covering prerequisites (Java 21, Maven), project structure, build instructions (`mvn clean package`), running demo website, running MCP server, running test scenarios, and viewing reports.

- [ ] **Step 5: Final Git Commit**

Run: `git add . && git commit -m "docs: add architecture diagrams, mcp examples, defect guide, and root readme"`
