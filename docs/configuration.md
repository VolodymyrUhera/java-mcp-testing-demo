# Configuration & Environment Reference

This document provides a complete guide to configuring runtime parameters, network ports, Maven properties, environment requirements, and output artifact paths for the project.

---

## ⚙️ Core Configuration Summary

| Parameter | Configuration Scope | Default Value | Description |
|-----------|---------------------|---------------|-------------|
| `DEFAULT_PORT` | `demo-website` | `8080` | Network port for `DemoWebServer`. Can be overridden via CLI argument. |
| `BASE_URL` | `testing-scenarios` | `http://localhost:8080` | Target URL for automated functional, performance, accessibility, and UX tests. |
| `java.version` | Parent `pom.xml` | `21` | Required Java Development Kit (JDK) source and target compilation version. |
| `playwright.version` | Parent `pom.xml` | `1.49.0` | Microsoft Playwright Java library and browser driver version. |
| `project.build.sourceEncoding` | Parent `pom.xml` | `UTF-8` | Source file character encoding across all modules. |

---

## 🖥️ Command-Line Arguments & Overrides

### 1. `DemoWebServer`

```bash
java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar [PORT]
```

* **`PORT` (Optional):** Integer specifying the HTTP listening port.
  * **Default:** `8080`
  * **Example:** `java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar 9090` (starts server on port `9090`).

### 2. `McpServer`

```bash
java -jar mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

* Operates strictly over `System.in` and `System.out`.
* Takes no command-line arguments. Protocol parameters are passed via JSON-RPC 2.0 requests over Stdio.

### 3. `MainTestPipeline`

```bash
java -jar testing-scenarios/target/testing-scenarios-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

* Executes test suites against `http://localhost:8080`.
* Outputs generated markdown reports to `reports/`.

---

## 📁 Output Artifact Paths

The automated testing pipeline generates output artifacts in the `reports/` folder at the project root:

| Report File | Format | Description |
|-------------|--------|-------------|
| `reports/functional_report.md` | Markdown | Route availability checks, HTTP status codes, form POST status, broken links table, broken images table. |
| `reports/performance_report.md` | Markdown | Metrics table containing load times (ms), DOMContentLoaded (ms), First Contentful Paint (FCP ms), and resource counts per page. |
| `reports/accessibility_report.md` | Markdown | Summary of WCAG accessibility defects found (`MISSING_ALT_TEXT`, `MISSING_FORM_LABEL`, `HEADING_HIERARCHY_SKIP`, `POOR_COLOR_CONTRAST`, `SMALL_CLICK_TARGET`). |
| `reports/ux_journey_report.md` | Markdown | Step-by-step latency log for multi-step UX navigation journey. |
| `reports/ux_journey_screenshot.png` | PNG Image | Full-page visual proof screenshot captured at the end of the UX journey. |

---

## 🔒 Security & Secrets Policy

* **No Secrets or Credentials:** This project does not use, require, or expose database passwords, API keys, JWT tokens, or private secrets.
* **Network Binding:** `DemoWebServer` binds to `InetSocketAddress(port)` on local interface. For production isolation, do not expose port `8080` to public networks without reverse proxy authentication.
