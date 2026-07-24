# Java Playwright MCP & Testing Demo — Documentation Index

Welcome to the technical documentation for the **Java Playwright Model Context Protocol (MCP) Demonstration & Automated Testing Suite**.

This documentation repository provides architectural overviews, detailed module descriptions, API references, execution flows, and developer guides based on the source code of the project.

---

## 📚 Documentation Directory

| Document | Description |
|----------|-------------|
| [Project README](../README.md) | Main project overview, features, quick start, and execution guide. |
| [Architecture](architecture.md) | High-level system design, process interaction model, and Mermaid sequence diagrams. |
| [Modules](modules.md) | Deep-dive documentation for `demo-website`, `mcp-server`, and `testing-scenarios`. |
| [Folder Structure](folder-structure.md) | Complete directory tree breakdown and module responsibility boundaries. |
| [Dependency Graph](dependency-graph.md) | Internal module dependencies, external libraries (Playwright), and JDK requirements. |
| [Runtime Flow](runtime.md) | Execution lifecycles for HTTP server, MCP Stdio protocol loop, and automated test pipeline. |
| [Configuration](configuration.md) | Runtime parameters, default settings, CLI options, and environment variables. |
| [Database](database.md) | Data persistence specifications and database usage notes. |
| [API Reference](api.md) | HTTP Web Server endpoints and MCP JSON-RPC 2.0 Stdio tool specifications. |
| [Development Guide](development.md) | Local development setup, Maven build commands, and code conventions. |
| [Deployment Guide](deployment.md) | Build artifact packaging (`jar-with-dependencies`), execution, and process isolation. |
| [Testing Framework](testing.md) | Automated functional, performance, accessibility, and UX journey testing details. |
| [Intentional Defects](intentional_defects.md) | Specification of engineered UX and WCAG accessibility flaws for audit testing. |
| [MCP Protocol Examples](mcp_examples.md) | Complete JSON-RPC request and response payload examples for all 12 MCP tools. |
| [Glossary](glossary.md) | Project-specific domain terminology, concepts, and technical acronyms. |

---

## 🔍 Visual Navigation & Knowledge Graph

This project has been analyzed using [Graphify](https://github.com/sponsors/safishamsi). You can explore the interactive visual graph of components, classes, and relationships:

* **Interactive Graph Visualization:** [`graphify-out/graph.html`](../graphify-out/graph.html)
* **Graph Audit & Community Report:** [`graphify-out/GRAPH_REPORT.md`](../graphify-out/GRAPH_REPORT.md)

---

## 🎯 Key Project Goals

1. **Java 21 Native MCP Implementation:** Provide a lightweight Model Context Protocol (MCP) server operating over Standard Input/Output (`stdio`) using zero external JSON dependencies.
2. **Playwright Integration:** Expose 12 browser automation and audit tools to AI agents and external clients via JSON-RPC 2.0.
3. **Retro HTTP Web Server:** Offer a standalone late-1990s retro web server using standard JDK HTTP server components, pre-seeded with intentional UX and accessibility defects.
4. **Automated Audit Pipeline:** Demonstrate end-to-end headless browser testing, automated WCAG accessibility defect identification, performance metrics collection, and UX journey proof generation.
