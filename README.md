# Java Playwright MCP Demonstration & Automated Testing Suite

A complete, production-grade demonstration project showcasing a **Java 21 Playwright Model Context Protocol (MCP) Server**, a **Standalone Late-1990s Themed Java HTTP Demo Website**, and an **Automated Functional, Performance, Accessibility, and UX Journey Testing Suite**.

---

## 🌟 Key Features

1. **Java Playwright MCP Server (`mcp-server`)**
   - Implements JSON-RPC 2.0 protocol over Standard I/O (Stdio).
   - Zero third-party web/JSON dependencies (uses custom lightweight stdlib JSON parser).
   - Exposes 12 browser automation tools:
     - `launch_browser`, `close_browser`, `open_url`, `navigate`, `click`, `fill_form`, `wait_for_selector`, `evaluate_javascript`, `take_screenshot`, `save_pdf`, `extract_content`, `get_performance_metrics`, `analyze_accessibility`.

2. **Retro Late-1990s Web Server (`demo-website`)**
   - Built using only `com.sun.net.httpserver.HttpServer` from Java 21 Standard Library.
   - Retro 1990s design: bright neon styling, table layouts, scrolling marquee, visitor counter, under-construction banners, fake awards, and guestbook form.
   - Engineered with intentional UX & WCAG accessibility defects for automated audit evaluation.

3. **Automated Testing Suite (`testing-scenarios`)**
   - Automated functional, performance, WCAG accessibility, and multi-step UX journey runners using Playwright.
   - Generates structured Markdown reports and visual PNG screenshots in `reports/`.

4. **Render Cloud Deployment & Render MCP Server Integration**
   - Multi-stage Docker packaging (`Dockerfile`) and Render Blueprint (`render.yaml`) for continuous cloud deployment.
   - Integration with the Render MCP server (`@niyogi/render-mcp`) in `.mcp.json` for AI-driven deployment monitoring, log streaming, and service management using `RENDER_API_KEY`.


---

## 📚 Complete Project Documentation

Full production-grade technical documentation is available in the [`docs/`](docs/README.md) directory:

| Document | Description |
|----------|-------------|
| 📖 [**Documentation Index**](docs/README.md) | Overview of all technical documentation and sitemap. |
| 🏛️ [**Architecture & Design**](docs/architecture.md) | High-level system design, sequence diagrams, and process isolation model. |
| 📦 [**Modules & Packages**](docs/modules.md) | Detailed breakdown of `demo-website`, `mcp-server`, and `testing-scenarios`. |
| 📂 [**Folder Structure**](docs/folder-structure.md) | Repository directory tree and component responsibility map. |
| 🔗 [**Dependency Graph**](docs/dependency-graph.md) | Maven POM dependencies, Playwright browser drivers, and JDK 21 requirements. |
| ⚡ [**Runtime Flow**](docs/runtime.md) | Execution lifecycles, virtual thread concurrency, and Stdio protocol loop. |
| ⚙️ [**Configuration**](docs/configuration.md) | Environment settings, network ports, CLI options, and artifact output paths. |
| 💾 [**Database Specification**](docs/database.md) | State model and persistence overview. |
| 🔌 [**API Reference**](docs/api.md) | HTTP Web Server endpoints and 12 MCP JSON-RPC 2.0 Stdio tool schemas. |
| 💻 [**Developer Guide**](docs/development.md) | Local setup, Maven build lifecycle (`mvn clean package`), and coding conventions. |
| 🚀 [**Deployment Guide**](docs/deployment.md) | Packaging fat JARs (`jar-with-dependencies`), Render cloud deployment (`render.yaml`, `Dockerfile`), and Render MCP server integration. |
| 🧪 [**Testing Framework**](docs/testing.md) | Automated functional, performance, accessibility, and UX journey testing details. |
| ⚠️ [**Intentional Defects**](docs/intentional_defects.md) | Specification of engineered UX and WCAG accessibility flaws for audit evaluation. |
| 📋 [**MCP Protocol Examples**](docs/mcp_examples.md) | JSON-RPC 2.0 request and response payload examples for all 12 MCP tools. |
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
├── demo-website/                # Standalone retro Java HTTP server
│   └── src/main/java/com/demo/website/
├── mcp-server/                  # Java Playwright MCP Server over Stdio
│   └── src/main/java/com/demo/mcp/
├── testing-scenarios/           # Automated test execution pipeline
│   └── src/main/java/com/demo/testing/
├── docs/                        # Complete project documentation
│   ├── README.md                # Documentation index
│   ├── architecture.md          # Architecture & Mermaid diagrams
│   ├── modules.md               # Module & package documentation
│   ├── folder-structure.md      # Directory map & responsibilities
│   ├── dependency-graph.md      # Dependency breakdown
│   ├── runtime.md               # Execution lifecycles
│   ├── configuration.md         # Runtime configuration
│   ├── database.md              # Database & persistence spec
│   ├── api.md                   # HTTP & MCP API reference
│   ├── development.md           # Developer guide & setup
│   ├── deployment.md            # Packaging & integration guide
│   ├── testing.md               # Automated testing framework
│   ├── intentional_defects.md   # Engineered UX & A11y defects
│   ├── mcp_examples.md          # Example MCP JSON-RPC messages
│   └── glossary.md              # Project terminology dictionary
├── reports/                     # Generated test reports & proof screenshots
│   ├── functional_report.md
│   ├── performance_report.md
│   ├── accessibility_report.md
│   ├── ux_journey_report.md
│   └── ux_journey_screenshot.png
└── graphify-out/                # Graphify visual knowledge graph
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

### 1. Run Demo Web Server Standalone

```bash
java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar
```
*Access the site at `http://localhost:8080` in your browser.*

### 2. Run MCP Server (Stdio Mode)

```bash
java -jar mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```
*The server will listen for JSON-RPC 2.0 requests on Standard Input (stdin) and reply on Standard Output (stdout).*

### 3. Run Automated Testing Suite Pipeline

```bash
java -jar testing-scenarios/target/testing-scenarios-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```
*This starts the local HTTP server, executes all test suites, outputs markdown reports in `reports/`, captures a UX journey proof screenshot, and gracefully closes.*

### 4. Deploy to Render Cloud & Run Render MCP Server

```bash
# Set environment variable for Render MCP integration
export RENDER_API_KEY="rnd_your_render_api_key_here"

# Build and run Docker image locally or deploy to Render via Blueprint (render.yaml)
docker build -t demo-website .
docker run -p 8080:8080 demo-website
```
*Render automatically builds and hosts the service using `render.yaml`. The `.mcp.json` file configures `@niyogi/render-mcp` alongside `java-playwright-mcp` for client host integration.*


---

## 📊 Viewing Test Reports

All test output reports are automatically saved to the `reports/` folder:

- **Functional Report:** [`reports/functional_report.md`](reports/functional_report.md)
- **Performance Metrics:** [`reports/performance_report.md`](reports/performance_report.md)
- **Accessibility Defect Audit:** [`reports/accessibility_report.md`](reports/accessibility_report.md)
- **UX Journey Execution Log:** [`reports/ux_journey_report.md`](reports/ux_journey_report.md)
- **UX Journey Proof Screenshot:** [`reports/ux_journey_screenshot.png`](reports/ux_journey_screenshot.png)
