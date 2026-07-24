# Java Test Runner Agent Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement pure Java test runner agent `TestRunnerAgent.java` in `testing-scenarios` module.

**Architecture:** A pure Java 21 agent class in `com.demo.testing.agent` package that coordinates `MainTestPipeline` execution, verifies `mcp-server` FAT JAR presence, parses test reports, and writes caveman summary output without any external Python or shell scripts.

**Tech Stack:** Java 21, Maven, Playwright, Stdio MCP Server.

## Global Constraints

- Zero Python (.py) or Shell (.sh) scripts allowed.
- Pure Java 21 Standard Library + existing project dependencies.
- Caveman summary formatting.

---

### Task 1: Implement `TestRunnerAgent.java`

**Files:**
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/TestRunnerAgent.java`

- [ ] **Step 1: Write `TestRunnerAgent.java`**

Create `testing-scenarios/src/main/java/com/demo/testing/agent/TestRunnerAgent.java` with package `com.demo.testing.agent`.
Implement:
- `main(String[] args)`
- `verifyMcpServerJar()`
- `executeTestPipeline()`
- `generateCavemanSummaryReport()`

- [ ] **Step 2: Compile project**

Run: `mvn compile -pl testing-scenarios`
Expected: BUILD SUCCESS

---

### Task 2: Verification Execution & Commit

**Files:**
- Output: `reports/agent_java_test_runner_summary.md`

- [ ] **Step 1: Run `TestRunnerAgent` via Maven**

Run: `mvn exec:java -Dexec.mainClass="com.demo.testing.agent.TestRunnerAgent" -pl testing-scenarios`
Expected: ALL TESTS PASSED CLEANLY & summary report generated.

- [ ] **Step 2: Commit implementation**

```bash
git add testing-scenarios/src/main/java/com/demo/testing/agent/TestRunnerAgent.java docs/superpowers/plans/2026-07-24-java-test-runner-agent.md
git commit -m "feat(agent): implement pure Java TestRunnerAgent"
```
