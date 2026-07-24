# Design Spec: Java Test Runner Agent (`TestRunnerAgent.java`)

**Date:** 2026-07-24  
**Status:** Approved  
**Author:** Software Engineering Sub-Agent  

---

## 1. Overview

Rewrite the `test_runner` agent into a pure Java 21 class `com.demo.testing.agent.TestRunnerAgent` residing within the `testing-scenarios` module. All shell (.sh) and Python (.py) scripts have been removed.

---

## 2. Architecture & Design Principles

1. **Pure Java 21 Implementation:** Zero external shell scripts or Python dependencies.
2. **Project-Structure Integration:** Placed directly in `testing-scenarios/src/main/java/com/demo/testing/agent/TestRunnerAgent.java`.
3. **Graphify Alignment:** Leverages existing project architecture—imports `mcp-server` JSON parser (`com.demo.mcp.json.*`) and `testing-scenarios` runners (`MainTestPipeline`, `TestReportGenerator`).
4. **Caveman Compression & Ponytail Minimalism:** Outputs terse, high-density test summary reports.

---

## 3. Class & Method Specification

- **Class:** `com.demo.testing.agent.TestRunnerAgent`
- **Location:** `testing-scenarios/src/main/java/com/demo/testing/agent/TestRunnerAgent.java`
- **Methods:**
  - `public static void main(String[] args)`: Entry point for running the agent via Maven (`mvn exec:java`).
  - `private void verifyMcpServerJar()`: Checks MCP FAT JAR availability.
  - `private void executeTestPipeline()`: Runs the test suite via `MainTestPipeline`.
  - `private void generateCavemanSummaryReport(long durationMs, boolean success)`: Parses test reports and writes summary to `reports/agent_java_test_runner_summary.md`.

---

## 4. Execution Command

Run directly using Maven:

```bash
mvn exec:java -Dexec.mainClass="com.demo.testing.agent.TestRunnerAgent" -pl testing-scenarios
```
