# Design Spec: Test Runner Subagent & Documentation

**Date:** 2026-07-24  
**Status:** Approved  
**Author:** Software Engineering Sub-Agent  

---

## 1. Overview

The `test_runner` subagent is a dedicated engineering subagent designed to execute automated testing scenarios in `testing-scenarios/`, interact directly with the `java-playwright-mcp` MCP server tools, analyze test logs and failure tracebacks, and generate caveman-compressed summary reports.

---

## 2. Architecture & Design Principles

1. **Superpowers & Systematic Debugging:** Employs empirical log inspection before diagnosing test failures or proposing code fixes.
2. **Caveman Compression:** All output reports and summaries follow full caveman style—technical precision retained, filler dropped.
3. **Ponytail Minimalism:** Adheres to YAGNI—no unneeded abstractions, minimal subagent tooling, uses existing Maven/Playwright/MCP infrastructure.

---

## 3. Subagent Configuration (`test_runner`)

- **Name:** `test_runner`
- **Description:** Executes testing-scenarios suite, calls MCP server tools, analyzes logs, and generates caveman summary reports.
- **Model Default:** `inherit`
- **Tool Grants:**
  - `enable_write_tools`: `true`
  - `enable_mcp_tools`: `true`
  - `enable_subagent_tools`: `false`

### System Prompt Requirements:
- Execute `mvn test` in `testing-scenarios/` via shell execution.
- Call `java-playwright-mcp` tools (`launch_browser`, `open_url`, `click`, `fill_form`, `take_screenshot`, `get_performance_metrics`, `analyze_accessibility`) when direct MCP interaction is needed.
- On test failure: parse exact stack trace, locate failing test line/source line, explain root cause.
- Format all outputs in caveman style.
- Enforce strict command safety (no destructive git/filesystem ops).

---

## 4. Documentation Artifact (`docs/test_runner_subagent.md`)

A comprehensive technical guide documenting:
1. **Overview & Architecture:** Component relations between Subagent, MCP Server, and `testing-scenarios`.
2. **Subagent Code Definition:** Full JSON/Tool payload used in `define_subagent`.
3. **Execution Lifecycle:** How subagent runs tests, calls MCP, inspects logs, and reports output.
4. **Usage Examples:** How to invoke `test_runner` via `invoke_subagent`.
5. **Defect Analysis Workflow:** How subagent isolates WCAG / Performance / Functional defects.

---

## 5. Verification & Testing

- Invoke `define_subagent` to register `test_runner`.
- Create `docs/test_runner_subagent.md`.
- Test invoke `test_runner` on `testing-scenarios` to verify execution and caveman reporting.
