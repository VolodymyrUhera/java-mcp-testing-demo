# Database & Persistence Specification

This document describes the data persistence architecture and state storage mechanisms of the project repository.

---

## 💾 Database Usage

> **Database Status:** No relational database (RDBMS), NoSQL database, or external object store is used in this repository.

All project modules operate purely in-memory or interface directly with the local filesystem for report generation:

1. **`demo-website` Module:**
   * Operates completely stateless in-memory using `com.sun.net.httpserver.HttpServer`.
   * Static assets (`style.css`, `under_construction.svg`) are read from classpath resource streams.
   * Contact form submissions (`POST /contact`) process form fields in-memory and return immediate confirmation HTML (`"Electronic Mail Sent!"`). Submissions are not persisted to a database.

2. **`mcp-server` Module:**
   * Operates in-memory.
   * Playwright browser contexts, pages, and tool schema definitions are stored in JVM memory during server runtime.
   * Artifact output tools (`take_screenshot`, `save_pdf`) write directly to the specified local filesystem path (e.g. `reports/screenshot.png`).

3. **`testing-scenarios` Module:**
   * Test metrics, page timing data, broken link lists, and accessibility defect maps are collected in standard Java collections (`Map`, `List`) in JVM memory during execution.
   * Generated test reports are written directly to local Markdown and PNG files in the `reports/` directory via `java.nio.file.Files.writeString(...)`.

---

## 📁 Filesystem Storage Summary

| Artifact / File Path | Persistence Mechanism | Lifecycle |
|----------------------|-----------------------|-----------|
| `reports/functional_report.md` | `java.nio.file.Files` | Written at end of `MainTestPipeline` execution. |
| `reports/performance_report.md` | `java.nio.file.Files` | Written at end of `MainTestPipeline` execution. |
| `reports/accessibility_report.md` | `java.nio.file.Files` | Written at end of `MainTestPipeline` execution. |
| `reports/ux_journey_report.md` | `java.nio.file.Files` | Written at end of `MainTestPipeline` execution. |
| `reports/ux_journey_screenshot.png` | Playwright `Page.screenshot` | Written during step 7 of `UxJourneyTestRunner`. |
