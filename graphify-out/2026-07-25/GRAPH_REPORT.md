# Graph Report - JiraMCP  (2026-07-25)

## Corpus Check
- 104 files · ~52,129 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 746 nodes · 1224 edges · 53 communities (44 shown, 9 thin omitted)
- Extraction: 88% EXTRACTED · 12% INFERRED · 0% AMBIGUOUS · INFERRED: 143 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `cc35d536`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- JSON Parsing Engine
- Demo HTTP Web Server
- Playwright Automation Manager
- JSON Model & Primitive Types
- Playwright & Main Execution Pipeline
- MCP Server Stdio Protocol
- Accessibility Audit Runner
- Performance Test Runner
- UX Journey Test Runner
- Test Report Generator
- Superpowers Implementation Plan
- Functional Test Output Report
- Performance Output Report
- UX Journey Output Report
- Parent Maven Module
- Demo Website Module
- MCP Server Module
- Testing Scenarios Module
- 2. Architecture & Components
- Runtime Flow & Execution Architecture
- Global Constraints
- Global Constraints
- Retro 90s MCP Server (`retro90s-mcp`)
- TestRunnerAgent
- Design Spec: Test Runner Subagent & Documentation
- Global Constraints
- Design Spec: Native Java Multi-Agent Orchestrator
- `test_runner` Subagent Technical Guide
- render
- Retro 90s MCP Server - Personality Prompt & Persona Guidelines
- Global Constraints
- Global Constraints
- Design Spec: Java Test Runner Agent (`TestRunnerAgent.java`)
- Functional Testing & Resource Audit Report
- Global Constraints
- Java Test Runner Agent Summary Report
- Java Multi-Agent Orchestrator Summary Report
- Test Runner Agent Summary Report
- Java Multi-Agent Orchestrator Summary Report
- Accessibility Audit Report
- UX Journey Test Report
- testing-scenarios/reports/performance_report.md
- retro90s-mcp
- .runPerformanceTests
- docs/README.md
- Runtime Flow & Execution Architecture
- Folder Structure & Directory Map
- Database & Persistence Specification
- Java Playwright MCP & Testing Demo — Documentation Index
- ToolRegistry
- Retro90sServer

## God Nodes (most connected - your core abstractions)
1. `JsonValue` - 37 edges
2. `Retro90sServer` - 28 edges
3. `ToolRegistry` - 27 edges
4. `KnowledgeService` - 26 edges
5. `PlaywrightManager` - 23 edges
6. `SearchService` - 22 edges
7. `KnowledgeItem` - 21 edges
8. `BaseAgent` - 19 edges
9. `🔤 Glossary Index` - 18 edges
10. `ToolRegistryTest` - 16 edges

## Surprising Connections (you probably didn't know these)
- `PlaywrightToolRegistry` --references--> `PlaywrightManager`  [EXTRACTED]
  mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightToolRegistry.java → mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightManager.java
- `Retro90sServer` --references--> `KnowledgeService`  [EXTRACTED]
  retro90s-mcp/src/main/java/com/retro90s/mcp/Retro90sServer.java → retro90s-mcp/src/main/java/com/retro90s/mcp/KnowledgeService.java
- `ToolRegistry` --references--> `KnowledgeService`  [EXTRACTED]
  retro90s-mcp/src/main/java/com/retro90s/mcp/ToolRegistry.java → retro90s-mcp/src/main/java/com/retro90s/mcp/KnowledgeService.java
- `Retro90sServer` --references--> `SearchService`  [EXTRACTED]
  retro90s-mcp/src/main/java/com/retro90s/mcp/Retro90sServer.java → retro90s-mcp/src/main/java/com/retro90s/mcp/SearchService.java
- `Retro90sServer` --references--> `ToolRegistry`  [EXTRACTED]
  retro90s-mcp/src/main/java/com/retro90s/mcp/Retro90sServer.java → retro90s-mcp/src/main/java/com/retro90s/mcp/ToolRegistry.java

## Import Cycles
- None detected.

## Communities (53 total, 9 thin omitted)

### Community 0 - "JSON Parsing Engine"
Cohesion: 0.09
Nodes (20): DemoWebServer, HttpServer, Logger, AboutHandler, HttpExchange, Override, ContactHandler, HttpExchange (+12 more)

### Community 1 - "Demo HTTP Web Server"
Cohesion: 0.08
Nodes (14): JsonParser, SuppressWarnings, JsonValue, Type, ARRAY, BOOLEAN, NULL, NUMBER (+6 more)

### Community 2 - "Playwright Automation Manager"
Cohesion: 0.06
Nodes (22): Browser, BrowserContext, Logger, Page, SuppressWarnings, PlaywrightManager, Playwright, AccessibilityTestRunner (+14 more)

### Community 3 - "JSON Model & Primitive Types"
Cohesion: 0.07
Nodes (13): AgentMessage, AgentMessageBus, Logger, BaseAgent, Override, DefectAnalyzerAgent, Override, Logger (+5 more)

### Community 4 - "Playwright & Main Execution Pipeline"
Cohesion: 0.11
Nodes (18): A, B, C, D, F, G, 🔤 Glossary Index, H (+10 more)

### Community 5 - "MCP Server Stdio Protocol"
Cohesion: 0.06
Nodes (30): 🌐 1. Module: `demo-website`, 🤖 2. Module: `mcp-server`, 🧪 3. Module: `testing-scenarios`, `AboutHandler` ([AboutHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/AboutHandler.java)), `AccessibilityTestRunner` ([AccessibilityTestRunner.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/AccessibilityTestRunner.java)), `ContactHandler` ([ContactHandler.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/handlers/ContactHandler.java)), `DemoWebServer` ([DemoWebServer.java](file:///home/voha/Documents/JiraMCP/demo-website/src/main/java/com/demo/website/DemoWebServer.java)), `FunctionalTestRunner` ([FunctionalTestRunner.java](file:///home/voha/Documents/JiraMCP/testing-scenarios/src/main/java/com/demo/testing/FunctionalTestRunner.java)) (+22 more)

### Community 6 - "Accessibility Audit Runner"
Cohesion: 0.07
Nodes (27): 10. `extract_content`, 11. `get_performance_metrics`, 12. `analyze_accessibility`, 1. `GET /`, 🌐 1. HTTP Web Server API (`demo-website`), 1. `launch_browser`, 2. `close_browser`, 2. `GET /about` (+19 more)

### Community 7 - "Performance Test Runner"
Cohesion: 0.09
Nodes (18): 1. Functional Test Suite (`FunctionalTestRunner`), 2. Performance Audit Suite (`PerformanceTestRunner`), 3. Accessibility Audit Suite (`AccessibilityTestRunner`), 4. UX Journey Test Suite (`UxJourneyTestRunner`), Automated Testing Framework Specification, 📈 Generated Markdown & PNG Output Files, 📊 Test Suite Breakdown, 🧪 Testing Framework Architecture (+10 more)

### Community 8 - "UX Journey Test Runner"
Cohesion: 0.12
Nodes (16): 1. Multi-Stage Docker Containerization (`Dockerfile`), 2. Render Blueprint Infrastructure-as-Code (`render.yaml`), 3. Render MCP Server Integration (`@niyogi/render-mcp`), Client Configuration Snippet (`claude_desktop_config.json` or `mcp_servers.json`), Deploying via Render Dashboard:, 📦 Deployment Artifact Summary, Deployment Guide & Packaging Specification, ⚙️ Integrating MCP Server into Client Agents (+8 more)

### Community 9 - "Test Report Generator"
Cohesion: 0.12
Nodes (8): DisplayName, KnowledgeItem, KnowledgeService, ObjectMapper, ResourceLoader, BeforeEach, Test, KnowledgeServiceTest

### Community 16 - "Parent Maven Module"
Cohesion: 0.15
Nodes (12): 1. Overview, 2.1 Color Palette & Variables, 2.2 Typography, 2.3 Retro Components & UI Elements, 2. Visual Architecture & Design System, 3.1 Backend Java Components, 3.2 Frontend Assets (`/static/`), 3. Technical Implementation Plan (+4 more)

### Community 17 - "Demo Website Module"
Cohesion: 0.15
Nodes (12): 1. Build Setup (`pom.xml`), 2. Transport Layer (`Retro90sServer.java`), 3. Knowledge Service (`KnowledgeService.java`), 4. Search Service (`SearchService.java`), 5. Tool Registry (`ToolRegistry.java`), 6. MCP Resources & Prompts, Design Document: `retro90s` MCP Server, Goals & Constraints (+4 more)

### Community 18 - "MCP Server Module"
Cohesion: 0.17
Nodes (11): 1. Full Project Clean & Build, 1. Run Demo Web Server, 2. Fast Build (Skip Tests / Direct Compile), 2. Run MCP Server (Stdio Interactive Mode), 3. Run Automated Test Pipeline, 🏗️ Build Lifecycle Commands, 💻 Code Conventions & Idioms, Developer Guide & Local Setup (+3 more)

### Community 19 - "Testing Scenarios Module"
Cohesion: 0.18
Nodes (10): Global Constraints, Retro 1999 Web Redesign Implementation Plan, Task 1: Retro Design System & Static CSS (`style.css`), Task 2: Enhanced Retro SVG Graphics (`/static/images/`), Task 3: Sidebar Navigation Helper & Shared Chrome (`NavigationHelper.java`), Task 4: Home Page Redesign (`HomeHandler.java`), Task 5: About Page Redesign (`AboutHandler.java`), Task 6: Contact & E-Mail Transmission Form (`ContactHandler.java`) (+2 more)

### Community 20 - "2. Architecture & Components"
Cohesion: 0.20
Nodes (9): 1. Overview & Objectives, 2.1 MCP Client Configuration (`.mcp.json`), 2.2 Cloud-Native Environment Adaptations (`DemoWebServer.java`), 2.3 Docker Packaging (`Dockerfile`), 2.4 Render Blueprint (`render.yaml`), 2. Architecture & Components, 3. Documentation Updates, 4. Verification Plan (+1 more)

### Community 21 - "Runtime Flow & Execution Architecture"
Cohesion: 0.22
Nodes (8): 1. `DemoWebServer`, 2. `McpServer`, 3. `MainTestPipeline`, 🖥️ Command-Line Arguments & Overrides, Configuration & Environment Reference, ⚙️ Core Configuration Summary, 📁 Output Artifact Paths, 🔒 Security & Secrets Policy

### Community 22 - "Global Constraints"
Cohesion: 0.22
Nodes (8): Global Constraints, Java MCP Server, Demo Website & Automated Testing Implementation Plan, Task 1: Project Scaffolding & Multi-Module Maven Setup, Task 2: Retro Late-1990s Demo Website (`demo-website`), Task 3: Lightweight JSON Parser & Java Playwright MCP Server (`mcp-server`), Task 4: Automated Testing Scenarios Suite (`testing-scenarios`), Task 5: End-to-End Pipeline Execution & Verification, Task 6: Documentation & Architecture Diagrams (`docs/` & `README.md`)

### Community 23 - "Global Constraints"
Cohesion: 0.22
Nodes (8): Global Constraints, Retro90s MCP Server Implementation Plan 🕹️✨, Spec & Plan Verification Self-Review, Task 1: Project Scaffolding & Knowledge Datasets 💾, Task 2: ResourceLoader & KnowledgeService Core 🧠, Task 3: Online Fallback SearchService 🔍, Task 4: ToolRegistry & 9 MCP Tools 🛠️, Task 5: HTTP SSE Server & Main Entrypoint 🚀

### Community 24 - "Retro 90s MCP Server (`retro90s-mcp`)"
Cohesion: 0.08
Nodes (24): 15 Datasets (`src/main/resources/knowledge/*.json`), 1. Connect to SSE Stream, 1. Establish SSE Connection, 2. Protocol Handshake (`initialize`), 2. Send JSON-RPC Requests, 3. List Available Tools (`tools/list`), 4. Call `ask90s` Tool (`tools/call`), 5. List Resources (`resources/list`) (+16 more)

### Community 26 - "Design Spec: Test Runner Subagent & Documentation"
Cohesion: 0.25
Nodes (7): 1. Overview, 2. Architecture & Design Principles, 3. Subagent Configuration (`test_runner`), 4. Documentation Artifact (`docs/test_runner_subagent.md`), 5. Verification & Testing, Design Spec: Test Runner Subagent & Documentation, System Prompt Requirements:

### Community 27 - "Global Constraints"
Cohesion: 0.29
Nodes (6): Global Constraints, Render MCP Integration & App Deployment Implementation Plan, Task 1: Add Cloud-Native `PORT` Environment Variable Support in `DemoWebServer.java`, Task 2: Create Containerization Config (`Dockerfile` & `.dockerignore`), Task 3: Configure Render Blueprint (`render.yaml`) & MCP Server (`.mcp.json`), Task 4: Update Documentation (`docs/deployment.md` & `README.md`)

### Community 28 - "Design Spec: Native Java Multi-Agent Orchestrator"
Cohesion: 0.29
Nodes (6): 1. Overview, 2. Architecture & Design Principles, 3. Component Breakdown, 4. Execution Command, Classes in `com.demo.testing.agent`:, Design Spec: Native Java Multi-Agent Orchestrator

### Community 29 - "`test_runner` Subagent Technical Guide"
Cohesion: 0.29
Nodes (6): 1. Overview & System Architecture, 2. Graphify Knowledge Graph Mapping, 3. Pure Java Subagent Implementation, 4. Execution Command, 5. Output Summary Report Format, `test_runner` Subagent Technical Guide

### Community 30 - "render"
Cohesion: 0.29
Nodes (6): RENDER_API_KEY, java, npx, java-playwright-mcp, render, @niyogi/render-mcp

### Community 31 - "Retro 90s MCP Server - Personality Prompt & Persona Guidelines"
Cohesion: 0.29
Nodes (6): Dial-Up & ASCII Signature Standard, Key Knowledge Domains, Output Directives, Persona Traits & Tone, Retro 90s MCP Server - Personality Prompt & Persona Guidelines, System Role Definition

### Community 32 - "Global Constraints"
Cohesion: 0.33
Nodes (5): Global Constraints, Java Multi-Agent Orchestrator Implementation Plan, Task 1: Message Bus Infrastructure, Task 2: Implement Autonomous Sub-Agents, Task 3: Implement `MultiAgentOrchestrator.java` & Verification

### Community 33 - "Global Constraints"
Cohesion: 0.33
Nodes (5): Global Constraints, Task 1: Register `test_runner` Subagent, Task 2: Create Technical Documentation (`docs/test_runner_subagent.md`), Task 3: Verification Execution, Test Runner Subagent Implementation Plan

### Community 34 - "Design Spec: Java Test Runner Agent (`TestRunnerAgent.java`)"
Cohesion: 0.33
Nodes (5): 1. Overview, 2. Architecture & Design Principles, 3. Class & Method Specification, 4. Execution Command, Design Spec: Java Test Runner Agent (`TestRunnerAgent.java`)

### Community 35 - "Functional Testing & Resource Audit Report"
Cohesion: 0.33
Nodes (5): Broken Images Audit, Broken Links Audit, Form Submissions, Functional Testing & Resource Audit Report, Route Availability Checks

### Community 36 - "Global Constraints"
Cohesion: 0.40
Nodes (4): Global Constraints, Java Test Runner Agent Implementation Plan, Task 1: Implement `TestRunnerAgent.java`, Task 2: Verification Execution & Commit

### Community 37 - "Java Test Runner Agent Summary Report"
Cohesion: 0.50
Nodes (3): Caveman Summary, Java Test Runner Agent Summary Report, Test Execution Details

### Community 38 - "Java Multi-Agent Orchestrator Summary Report"
Cohesion: 0.50
Nodes (3): Caveman Summary, Inter-Agent Event Log, Java Multi-Agent Orchestrator Summary Report

### Community 39 - "Test Runner Agent Summary Report"
Cohesion: 0.50
Nodes (3): Caveman Summary, Test Execution Details, Test Runner Agent Summary Report

### Community 40 - "Java Multi-Agent Orchestrator Summary Report"
Cohesion: 0.50
Nodes (3): Caveman Summary, Inter-Agent Event Log, Java Multi-Agent Orchestrator Summary Report

### Community 45 - ".runPerformanceTests"
Cohesion: 0.12
Nodes (13): AfterEach, Pattern, HttpClient, JsonNode, ObjectMapper, SearchService, DynamicHandler, BeforeEach (+5 more)

### Community 46 - "docs/README.md"
Cohesion: 0.20
Nodes (6): 🛠️ Build System & Maven Plugins, Dependency Graph & Package Management, 📋 Dependency Specification Table, 🌐 External Runtime Dependencies, 📦 Project Dependency Architecture, Project Terminology & Technical Glossary

### Community 47 - "Runtime Flow & Execution Architecture"
Cohesion: 0.25
Nodes (8): 🌐 1. Standalone HTTP Web Server Runtime Flow, 🤖 2. MCP Server Stdio Protocol Loop, 🧪 3. Automated Test Execution Pipeline Flow, ⚡ Execution Lifecycles, Runtime Flow & Execution Architecture, Stdio Communication Protocol Specifications, 🔒 Synchronous Safeguards & Cleanup Hooks, Threading & Concurrency Model

### Community 48 - "Folder Structure & Directory Map"
Cohesion: 0.40
Nodes (4): Breakdown of Primary Directories, 🏛️ Directory Responsibilities & Relationships, Folder Structure & Directory Map, 📂 Repository File Tree

### Community 49 - "Database & Persistence Specification"
Cohesion: 0.50
Nodes (3): Database & Persistence Specification, 💾 Database Usage, 📁 Filesystem Storage Summary

### Community 50 - "Java Playwright MCP & Testing Demo — Documentation Index"
Cohesion: 0.50
Nodes (4): 📚 Documentation Directory, Java Playwright MCP & Testing Demo — Documentation Index, 🎯 Key Project Goals, 🔍 Visual Navigation & Knowledge Graph

### Community 51 - "ToolRegistry"
Cohesion: 0.14
Nodes (9): ArrayNode, JsonNode, ObjectMapper, ObjectNode, ToolRegistry, BeforeEach, ObjectMapper, Test (+1 more)

### Community 52 - "Retro90sServer"
Cohesion: 0.09
Nodes (17): AfterAll, BeforeAll, Main, HttpExchange, HttpServer, JsonNode, ObjectMapper, ObjectNode (+9 more)

## Knowledge Gaps
- **240 isolated node(s):** `java`, `npx`, `@niyogi/render-mcp`, `RENDER_API_KEY`, `demo-website` (+235 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **9 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Retro90sServer` connect `Retro90sServer` to `Test Report Generator`, `ToolRegistry`, `.runPerformanceTests`?**
  _High betweenness centrality (0.106) - this node is a cross-community bridge._
- **Why does `PlaywrightManager` connect `Playwright Automation Manager` to `Demo HTTP Web Server`?**
  _High betweenness centrality (0.059) - this node is a cross-community bridge._
- **What connects `java`, `npx`, `@niyogi/render-mcp` to the rest of the system?**
  _240 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `JSON Parsing Engine` be split into smaller, more focused modules?**
  _Cohesion score 0.08961593172119488 - nodes in this community are weakly interconnected._
- **Should `Demo HTTP Web Server` be split into smaller, more focused modules?**
  _Cohesion score 0.08455625436757512 - nodes in this community are weakly interconnected._
- **Should `Playwright Automation Manager` be split into smaller, more focused modules?**
  _Cohesion score 0.06265664160401002 - nodes in this community are weakly interconnected._
- **Should `JSON Model & Primitive Types` be split into smaller, more focused modules?**
  _Cohesion score 0.07058823529411765 - nodes in this community are weakly interconnected._