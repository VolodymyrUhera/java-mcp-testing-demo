# Java Multi-Agent Orchestrator Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement native Java 21 multi-agent orchestrator with Virtual Threads and in-memory message bus in `testing-scenarios` module.

**Architecture:** An in-memory pub/sub message bus (`AgentMessageBus`) connecting three autonomous agents (`TestExecutorAgent`, `DefectAnalyzerAgent`, `ReportAggregatorAgent`) running concurrently on Java 21 Virtual Threads, coordinated by `MultiAgentOrchestrator`.

**Tech Stack:** Java 21, Virtual Threads, Concurrent Collections, Maven, Playwright.

## Global Constraints

- Pure Java 21 standard library + existing project dependencies.
- Caveman output reporting.
- Zero external frameworks.

---

### Task 1: Message Bus Infrastructure

**Files:**
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/AgentMessage.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/AgentMessageBus.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/BaseAgent.java`

- [ ] **Step 1: Write `AgentMessage.java`** (Record)
- [ ] **Step 2: Write `AgentMessageBus.java`** (Thread-safe message router)
- [ ] **Step 3: Write `BaseAgent.java`** (Virtual thread runnable agent)

---

### Task 2: Implement Autonomous Sub-Agents

**Files:**
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/TestExecutorAgent.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/DefectAnalyzerAgent.java`
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/ReportAggregatorAgent.java`

- [ ] **Step 1: Write `TestExecutorAgent.java`**
- [ ] **Step 2: Write `DefectAnalyzerAgent.java`**
- [ ] **Step 3: Write `ReportAggregatorAgent.java`**

---

### Task 3: Implement `MultiAgentOrchestrator.java` & Verification

**Files:**
- Create: `testing-scenarios/src/main/java/com/demo/testing/agent/MultiAgentOrchestrator.java`

- [ ] **Step 1: Write `MultiAgentOrchestrator.java`**
- [ ] **Step 2: Compile & Run via Maven**

Run: `mvn exec:java -Dexec.mainClass="com.demo.testing.agent.MultiAgentOrchestrator" -pl testing-scenarios`
Expected: Multi-agent system executes, exchanges messages, and writes report to `reports/agent_multiagent_summary.md`.

- [ ] **Step 3: Commit implementation**

```bash
git add testing-scenarios/src/main/java/com/demo/testing/agent/ docs/superpowers/plans/2026-07-24-java-multi-agent-orchestrator.md
git commit -m "feat(agent): implement native Java 21 multi-agent orchestrator"
```
