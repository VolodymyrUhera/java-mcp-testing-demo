# Project Architecture & Systems Design

This document details the system design, component architecture, module relationships, interaction sequences, and process isolation model for the **Java Playwright MCP Demonstration & Automated Testing Suite**.

---

## 🏛️ System Architecture Overview

The system is designed as a multi-module Java 21 repository consisting of three decoupled core components:

1. **`demo-website`**: A standalone HTTP web server serving a retro late-1990s website using JDK's built-in `com.sun.net.httpserver.HttpServer`.
2. **`mcp-server`**: A Model Context Protocol (MCP) server communicating over Standard Input/Output (`stdio`) using JSON-RPC 2.0. It bridges MCP clients (e.g. LLM agents) to a headless Chromium browser powered by Microsoft Playwright Java.
3. **`testing-scenarios`**: An automated end-to-end test execution pipeline that orchestrates the `demo-website` and `mcp-server` / Playwright API to run functional, performance, accessibility, and multi-step UX journey audits.

```mermaid
graph TD
    subgraph External Clients & Protocols
        Client[MCP Client / LLM Agent / CLI]
    end

    subgraph mcp-server Module
        Stdio[Stdio Interface stdin/stdout]
        MCPServer[McpServer Loop]
        JsonParser[Custom JsonParser & JsonValue]
        Registry[PlaywrightToolRegistry]
        PWManager[PlaywrightManager]
    end

    subgraph Playwright Engine
        PlaywrightCore[Microsoft Playwright Engine]
        BrowserInstance[Headless Chromium Browser]
    end

    subgraph demo-website Module
        DemoServer[DemoWebServer - Port 8080]
        Home[HomeHandler /]
        About[AboutHandler /about]
        Contact[ContactHandler /contact]
        Links[LinksHandler /links]
        Static[StaticAssetHandler /static/*]
    end

    subgraph testing-scenarios Module
        Pipeline[MainTestPipeline]
        FuncRunner[FunctionalTestRunner]
        PerfRunner[PerformanceTestRunner]
        A11yRunner[AccessibilityTestRunner]
        UxRunner[UxJourneyTestRunner]
        ReportGen[TestReportGenerator]
    end

    subgraph Reports Output
        ReportsDir[reports/ Directory]
    end

    Client <-->|JSON-RPC 2.0 over Stdio| Stdio
    Stdio <--> MCPServer
    MCPServer --> JsonParser
    MCPServer --> Registry
    Registry --> PWManager
    PWManager --> PlaywrightCore
    PlaywrightCore --> BrowserInstance

    Pipeline --> DemoServer
    Pipeline --> FuncRunner
    Pipeline --> PerfRunner
    Pipeline --> A11yRunner
    Pipeline --> UxRunner
    Pipeline --> ReportGen

    FuncRunner -->|HttpClient & Playwright| DemoServer
    PerfRunner -->|Playwright JS Eval| DemoServer
    A11yRunner -->|Playwright DOM Audit| DemoServer
    UxRunner -->|Playwright Navigation| DemoServer

    ReportGen -->|Outputs Markdown & PNG| ReportsDir
```

---

## 📦 Component Relationships & Module Boundaries

```mermaid
classDiagram
    class DemoWebServer {
        -HttpServer server
        -int port
        +start() void
        +stop() void
        +getPort() int
        +main(args) void
    }

    class McpServer {
        +main(args) void
    }

    class PlaywrightToolRegistry {
        -PlaywrightManager playwrightManager
        +getToolDefinitions() List~JsonValue~
        +executeTool(name, args) String
    }

    class PlaywrightManager {
        -Playwright playwright
        -Browser browser
        -BrowserContext context
        -Page page
        +launchBrowser(headless) void
        +closeBrowser() void
        +navigate(url) String
        +click(selector) String
        +fillForm(selector, value) String
        +waitForSelector(selector, timeoutMs) String
        +evaluateJavascript(script) Object
        +takeScreenshot(filePath) String
        +savePdf(filePath) String
        +extractContent() Map
        +getPerformanceMetrics() Map
        +analyzeAccessibility() Map
    }

    class MainTestPipeline {
        +main(args) void
    }

    McpServer --> PlaywrightToolRegistry
    PlaywrightToolRegistry --> PlaywrightManager
    MainTestPipeline --> DemoWebServer
    MainTestPipeline --> PlaywrightManager
```

---

## 🔄 Interaction Sequence Diagrams

### 1. MCP Tool Call Sequence (Stdio Mode)

The following diagram illustrates how an external MCP client (or LLM agent) invokes a browser automation tool (`open_url`) via Standard Input/Output:

```mermaid
sequenceDiagram
    autonumber
    participant Client as MCP Client (Stdio)
    participant Server as McpServer Loop
    participant Parser as JsonParser
    participant Registry as PlaywrightToolRegistry
    participant Manager as PlaywrightManager
    participant Browser as Chromium Browser

    Client->>Server: {"jsonrpc":"2.0","id":1,"method":"tools/call","params":{"name":"open_url","arguments":{"url":"http://localhost:8080"}}}
    Server->>Parser: parse(line)
    Parser-->>Server: JsonValue (Request Object)
    Server->>Registry: executeTool("open_url", args)
    Registry->>Manager: navigate("http://localhost:8080")
    Manager->>Browser: page.navigate("http://localhost:8080")
    Browser-->>Manager: Response [200 OK]
    Manager-->>Registry: "Navigated to http://localhost:8080 [Status: 200]"
    Registry-->>Server: Output Result String
    Server-->>Client: {"jsonrpc":"2.0","id":1,"result":{"content":[{"type":"text","text":"Navigated to http://localhost:8080 [Status: 200]"}]}}
```

### 2. Automated Test Suite Pipeline Sequence

The following diagram details the sequence executed when running `MainTestPipeline`:

```mermaid
sequenceDiagram
    autonumber
    participant Pipeline as MainTestPipeline
    participant Server as DemoWebServer (Port 8080)
    participant PW as Playwright API / Page
    participant Func as FunctionalTestRunner
    participant Perf as PerformanceTestRunner
    participant A11y as AccessibilityTestRunner
    participant Ux as UxJourneyTestRunner
    participant Report as TestReportGenerator

    Pipeline->>Server: start() (Listening on :8080)
    Pipeline->>PW: Playwright.create() & launch Chromium
    
    Pipeline->>Func: runFunctionalTests(page)
    Func->>PW: Test routes /, /about, /contact, /links & form POST
    Func-->>Pipeline: Functional Test Results

    Pipeline->>Perf: runPerformanceTests(page)
    Perf->>PW: Evaluate window.performance metrics
    Perf-->>Pipeline: Performance Metrics Map

    Pipeline->>A11y: runAccessibilityAudit(page)
    A11y->>PW: Audit ALT tags, labels, headings, contrast
    A11y-->>Pipeline: Accessibility Defect Map

    Pipeline->>Ux: runUxJourney(page)
    Ux->>PW: Multi-step click journey & screenshot
    Ux-->>Pipeline: UX Journey Steps & Latency

    Pipeline->>Report: generateReports(...)
    Report-->>Pipeline: Markdown files & PNG written to reports/

    Pipeline->>PW: browser.close() & playwright.close()
    Pipeline->>Server: stop()
```

---

## 🖥️ Runtime & Deployment Isolation Model

```mermaid
graph LR
    subgraph Host OS Environment (Linux / macOS / Windows)
        JVM[Java 21 Virtual Machine]
        
        subgraph Process 1: Demo Web Server
            HttpServer[com.sun.net.httpserver.HttpServer :8080]
            Executors[Virtual Thread Per Task Executor]
            HttpServer --- Executors
        end
        
        subgraph Process 2: MCP Server / Test Execution
            McpLoop[McpServer Stdio Reader Loop]
            PWDriver[Playwright Java Driver Node Subprocess]
            Chromium[Chromium Headless Process]
            
            McpLoop --> PWDriver
            PWDriver --> Chromium
        end
        
        JVM --> Process 1
        JVM --> Process 2
    end
```

---

## 🔬 Architectural Key Decisions & Trade-offs

1. **Zero-Dependency JSON Parsing (`mcp-server`):**
   - **Decision:** Implemented a minimal custom recursive-descent parser (`JsonParser`, `JsonValue`).
   - **Rationale:** Avoids heavy third-party JSON dependencies (like Jackson or Gson), making the `mcp-server` lightweight, self-contained, and fast to initialize.
2. **JDK Native HTTP Server (`demo-website`):**
   - **Decision:** Used `com.sun.net.httpserver.HttpServer` with Java 21 `Executors.newVirtualThreadPerTaskExecutor()`.
   - **Rationale:** Eliminates external framework overhead (Spring Boot, Micronaut, Netty) while utilizing modern virtual threads for high concurrency and clean lifecycle management.
3. **Synchronous Stdio Loop (`mcp-server`):**
   - **Decision:** Read `System.in` line by line and output JSON-RPC 2.0 messages directly to `System.out`.
   - **Rationale:** Strictly conforms to the standard Model Context Protocol Stdio transport specification.
