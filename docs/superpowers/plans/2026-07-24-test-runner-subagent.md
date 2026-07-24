# Test Runner Subagent Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Register the `test_runner` subagent and create comprehensive technical documentation in `docs/test_runner_subagent.md`.

**Architecture:** The `test_runner` subagent is registered via `define_subagent` with write and MCP capabilities. Comprehensive documentation detailing its architecture, configuration, system prompt, lifecycle, and usage is authored in `docs/test_runner_subagent.md`.

**Tech Stack:** Java 21, Maven, Playwright, MCP JSON-RPC Stdio Server, Gemini / AI Client Subagents.

## Global Constraints

- No external libraries introduced.
- Strict Caveman compression for summaries and reports.
- Ponytail YAGNI minimalist design.
- Complete documentation of subagent setup and operation.

---

### Task 1: Register `test_runner` Subagent

**Files:**
- System state (register subagent via `define_subagent`)

- [ ] **Step 1: Define `test_runner` subagent**

Call `define_subagent` with:
- `name`: `test_runner`
- `description`: "Automated test execution subagent for running testing scenarios, inspecting reports, using MCP server tools, analyzing defects, and generating caveman-compressed test summaries."
- `enable_write_tools`: `true`
- `enable_mcp_tools`: `true`
- `enable_subagent_tools`: `false`
- `system_prompt`: Full subagent system prompt enforcing test execution, log inspection, MCP tools, and caveman reporting.

- [ ] **Step 2: Verify subagent registration**

Run `manage_subagents` with action `list` to confirm registration.

---

### Task 2: Create Technical Documentation (`docs/test_runner_subagent.md`)

**Files:**
- Create: `docs/test_runner_subagent.md`

- [ ] **Step 1: Write `docs/test_runner_subagent.md`**

Write documentation containing:
- Overview & System Architecture
- Subagent Configuration & System Prompt Breakdown
- Step-by-Step Execution Lifecycle
- Invocation & Usage Examples
- Defect Root-Cause Analysis Workflow

- [ ] **Step 2: Commit documentation to git**

```bash
git add docs/test_runner_subagent.md
git commit -m "docs: add test_runner subagent technical guide"
```

---

### Task 3: Verification Execution

**Files:**
- Run subagent `test_runner` via `invoke_subagent`

- [ ] **Step 1: Invoke `test_runner` subagent**

Pass prompt to `test_runner`: "Run testing-scenarios suite using `mvn test` and generate caveman-compressed test report."

- [ ] **Step 2: Verify output and report**

Confirm subagent completed cleanly and generated report in caveman format.
