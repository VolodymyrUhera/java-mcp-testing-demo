# Java Playwright & Retro 90s MCP Demonstration & Automated Testing Suite

A complete, production-grade demonstration project showcasing a **Java 21 Playwright Model Context Protocol (MCP) Server**, a **Dedicated 1990s Retro Knowledge MCP Server (`retro90s-mcp`)**, a **Standalone Late-1990s Themed Java HTTP Demo Website**, and an **Automated Functional, Performance, Accessibility, and UX Journey Testing Suite**.

---

## 🌟 Key Features

1. **Retro 90s Knowledge MCP Server (`retro90s-mcp`)** 🕹️⚡
   - Framework-free Java 21 `com.sun.net.httpserver` HTTP + SSE server.
   - 15 offline JSON knowledge datasets covering 90s computing, software, hardware, pop culture, media, and history.
   - 9 specialized MCP tools (`ask90s`, `compare`, `recommend`, `explain`, `trivia`, `nostalgia`, `year`, `website`, `hardware`).
   - 5 MCP resources (`retro90s://timeline`, `retro90s://operating-systems`, `retro90s://consoles`, `retro90s://programming`, `retro90s://internet`).
   - Smart fallback web search via Wikipedia REST & DuckDuckGo APIs using Java 21 `HttpClient`.
   - Cyber-Steve 90s expert personality prompt (`personality.md`).

2. **Java Playwright MCP Server (`mcp-server`)** 🌐
   - Implements JSON-RPC 2.0 protocol over Standard I/O (Stdio).
   - Zero third-party web/JSON dependencies (uses custom lightweight stdlib JSON parser).
   - Exposes 12 browser automation tools:
     - `launch_browser`, `close_browser`, `open_url`, `navigate`, `click`, `fill_form`, `wait_for_selector`, `evaluate_javascript`, `take_screenshot`, `save_pdf`, `extract_content`, `get_performance_metrics`, `analyze_accessibility`.

3. **Retro Late-1990s Web Server (`demo-website`)** 📼
   - Built using only `com.sun.net.httpserver.HttpServer` from Java 21 Standard Library.
   - Retro 1990s design: bright neon styling, table layouts, scrolling marquee, visitor counter, under-construction banners, fake awards, and guestbook form.
   - Engineered with intentional UX & WCAG accessibility defects for automated audit evaluation.

4. **Automated Testing Suite (`testing-scenarios`)** 🧪
   - Automated functional, performance, WCAG accessibility, and multi-step UX journey runners using Playwright.
   - Generates structured Markdown reports and visual PNG screenshots in `reports/`.

5. **Render Cloud Deployment & Render MCP Server Integration** ☁️
   - Multi-stage Docker packaging (`Dockerfile`) and Render Blueprint (`render.yaml`) for continuous cloud deployment.
   - Integration with the Render MCP server (`@niyogi/render-mcp`) in `.mcp.json` for AI-driven deployment monitoring, log streaming, and service management using `RENDER_API_KEY`.

---

## 📚 Complete Project Documentation

Full production-grade technical documentation is available in the [`docs/`](docs/README.md) directory:

| Document | Description |
|----------|-------------|
| 📖 [**Documentation Index**](docs/README.md) | Overview of all technical documentation and sitemap. |
| 🕹️ [**Retro 90s MCP Server**](retro90s-mcp/README.md) | Full architectural, tool, resource, and endpoint guide for `retro90s-mcp`. |
| 🏛️ [**Architecture & Design**](docs/architecture.md) | High-level system design, sequence diagrams, and process isolation model. |
| 📦 [**Modules & Packages**](docs/modules.md) | Detailed breakdown of `demo-website`, `mcp-server`, `retro90s-mcp`, and `testing-scenarios`. |
| 📂 [**Folder Structure**](docs/folder-structure.md) | Repository directory tree and component responsibility map. |
| 🔗 [**Dependency Graph**](docs/dependency-graph.md) | Maven POM dependencies, Playwright browser drivers, and JDK 21 requirements. |
| ⚡ [**Runtime Flow**](docs/runtime.md) | Execution lifecycles, virtual thread concurrency, Stdio, and SSE protocol loops. |
| ⚙️ [**Configuration**](docs/configuration.md) | Environment settings, network ports, CLI options, and artifact output paths. |
| 💾 [**Database Specification**](docs/database.md) | State model and persistence overview. |
| 🔌 [**API Reference**](docs/api.md) | HTTP Web Server endpoints and MCP JSON-RPC 2.0 Stdio & SSE tool schemas. |
| 💻 [**Developer Guide**](docs/development.md) | Local setup, Maven build lifecycle (`mvn clean package`), and coding conventions. |
| 🚀 [**Deployment Guide**](docs/deployment.md) | Packaging fat JARs, Render cloud deployment, and Render MCP server integration. |
| 🧪 [**Testing Framework**](docs/testing.md) | Automated functional, performance, accessibility, and UX journey testing details. |
| ⚠️ [**Intentional Defects**](docs/intentional_defects.md) | Specification of engineered UX and WCAG accessibility flaws for audit evaluation. |
| 📋 [**MCP Protocol Examples**](docs/mcp_examples.md) | JSON-RPC 2.0 request and response payload examples for all MCP tools. |
| 🔤 [**Technical Glossary**](docs/glossary.md) | Technical terms, concepts, and acronym dictionary. |

---

## 🔍 Visual Knowledge Graph (Graphify)

This repository includes a persistent knowledge graph generated using [Graphify](https://github.com/sponsors/safishamsi):

* **Interactive Graph Visualization:** [`graphify-out/graph.html`](graphify-out/graph.html)
* **Graph Audit & Community Report:** [`graphify-out/GRAPH_REPORT.md`](graphify-out/GRAPH_REPORT.md)

---

## 📁 Project Structure

```
project-root/
├── pom.xml                      # Parent Maven POM (Java 21)
├── retro90s-mcp/                # Dedicated 1990s Retro Knowledge MCP HTTP SSE Server
│   ├── pom.xml
│   ├── README.md
│   └── src/
│       ├── main/java/com/retro90s/mcp/
│       └── main/resources/
│           ├── knowledge/*.json # 15 category JSON knowledge bases
│           └── prompts/personality.md
├── demo-website/                # Standalone retro Java HTTP server
│   └── src/main/java/com/demo/website/
├── mcp-server/                  # Java Playwright MCP Server over Stdio
│   └── src/main/java/com/demo/mcp/
├── testing-scenarios/           # Automated test execution pipeline
│   └── src/main/java/com/demo/testing/
├── docs/                        # Complete project documentation
│   ├── README.md                # Documentation index
│   ├── architecture.md          # Architecture & Mermaid diagrams
│   └── ...
├── reports/                     # Generated test reports & proof screenshots
└── graphify-out/                # Graphify visual knowledge graph (746 nodes, 53 communities)
    ├── graph.html
    ├── graph.json
    └── GRAPH_REPORT.md
```

---

## 🛠️ Prerequisites & Setup

- **Java Development Kit (JDK):** Version 21 or higher (`java -version`).
- **Build System:** Apache Maven 3.8+ (`mvn -version`).

### Build Project

To compile all modules and build executable JAR packages, run:

```bash
mvn clean package
```

---

## 🚀 Execution Instructions

### 1. Run Retro 90s MCP Server (HTTP SSE Mode)

```bash
java -jar retro90s-mcp/target/retro90s-mcp-1.0.0-SNAPSHOT.jar
```
*Access SSE stream at `http://localhost:8080/sse` and POST messages to `http://localhost:8080/message`.*

### 2. Run Demo Web Server Standalone

```bash
java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar
```
*Access the site at `http://localhost:8080` in your browser.*

### 3. Run Java Playwright MCP Server (Stdio Mode)

```bash
java -jar mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

### 4. Run Automated Testing Suite Pipeline

```bash
java -jar testing-scenarios/target/testing-scenarios-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 📊 Viewing Test Reports

All test output reports are automatically saved to the `reports/` folder:

- **Functional Report:** [`reports/functional_report.md`](reports/functional_report.md)
- **Performance Metrics:** [`reports/performance_report.md`](reports/performance_report.md)
- **Accessibility Defect Audit:** [`reports/accessibility_report.md`](reports/accessibility_report.md)
- **UX Journey Execution Log:** [`reports/ux_journey_report.md`](reports/ux_journey_report.md)
- **UX Journey Proof Screenshot:** [`reports/ux_journey_screenshot.png`](reports/ux_journey_screenshot.png)
