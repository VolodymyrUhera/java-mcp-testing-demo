# Developer Guide & Local Setup

This document provides instructions for developers setting up, building, running, and extending the repository locally.

---

## 🛠️ System Prerequisites

Ensure your development machine has the following tools installed:

1. **Java Development Kit (JDK):** Version `21` or higher.
   * Verify version: `java -version` (Must output `21` or higher).
2. **Apache Maven:** Version `3.8.0` or higher.
   * Verify version: `mvn -version`.
3. **Operating System:** Linux, macOS, or Windows.
4. **Browser Requirements:** None required. Microsoft Playwright downloads its required Chromium browser binaries automatically into user cache during initial execution.

---

## 🏗️ Build Lifecycle Commands

The repository uses Maven multi-module builds. Execute commands from the repository root:

```bash
cd /path/to/JiraMCP
```

### 1. Full Project Clean & Build

Compile all modules, run compiler checks, and create executable JAR artifacts:

```bash
mvn clean package
```

Upon completion, the target executable artifacts will be produced:

* `demo-website/target/demo-website-1.0.0-SNAPSHOT.jar`
* `mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar`
* `testing-scenarios/target/testing-scenarios-1.0.0-SNAPSHOT-jar-with-dependencies.jar`

### 2. Fast Build (Skip Tests / Direct Compile)

```bash
mvn clean compile
```

---

## 🚀 Running Components Locally

### 1. Run Demo Web Server

Starts the HTTP server on `http://localhost:8080`:

```bash
java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar
```

Or pass a custom port:

```bash
java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar 9090
```

### 2. Run MCP Server (Stdio Interactive Mode)

Starts the MCP server reading JSON-RPC 2.0 requests from `stdin`:

```bash
java -jar mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

To test manually, paste a JSON-RPC request into standard input and press Enter:

```json
{"jsonrpc":"2.0","id":1,"method":"initialize","params":{}}
```

### 3. Run Automated Test Pipeline

Executes the full automated testing suite (`MainTestPipeline`), starting the web server on port 8080, launching Playwright, executing audits, and generating output files in `reports/`:

```bash
java -jar testing-scenarios/target/testing-scenarios-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 💻 Code Conventions & Idioms

When contributing to this repository, follow these codebase standards:

1. **Modern Java 21 Syntax:**
   * Use **Text Blocks** (`""" ... """`) for multi-line HTML templates, SQL/JS scripts, and formatted strings.
   * Use **Switch Expressions** (`switch (name) { case "foo" -> ... }`) for tool dispatching in `PlaywrightToolRegistry`.
   * Use **Virtual Threads** (`Executors.newVirtualThreadPerTaskExecutor()`) for high-concurrency server tasks.
2. **Zero Dependencies in Core Web Server:**
   * Do not introduce Jackson, Spring, or external web frameworks into `demo-website`. Maintain usage of JDK standard library `com.sun.net.httpserver.HttpServer`.
3. **Zero Dependencies in JSON Parser:**
   * Maintain custom lightweight `JsonParser` and `JsonValue` classes in `mcp-server` to keep payload parsing dependency-free.
4. **Logging Discipline:**
   * Use `java.util.logging.Logger`. In `mcp-server`, never print log messages to `System.out` as `System.out` is reserved exclusively for JSON-RPC messages.

---

## 🔍 IDE Configuration Tips

* **IntelliJ IDEA / Eclipse / VS Code:**
  * Open the repository root folder containing parent `pom.xml`.
  * Set SDK / Language Level to **21**.
  * Enable Annotation Processing if prompted.
