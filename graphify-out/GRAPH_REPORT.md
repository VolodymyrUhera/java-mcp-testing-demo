# Graph Report - .  (2026-07-24)

## Corpus Check
- Corpus is ~11,471 words - fits in a single context window. You may not need a graph.

## Summary
- 185 nodes · 377 edges · 16 communities (12 shown, 4 thin omitted)
- Extraction: 80% EXTRACTED · 20% INFERRED · 0% AMBIGUOUS · INFERRED: 75 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_JSON Parsing Engine|JSON Parsing Engine]]
- [[_COMMUNITY_Demo HTTP Web Server & Handlers|Demo HTTP Web Server & Handlers]]
- [[_COMMUNITY_Playwright Automation Manager|Playwright Automation Manager]]
- [[_COMMUNITY_JSON Model & Primitive Types|JSON Model & Primitive Types]]
- [[_COMMUNITY_Playwright & Main Execution Pipeline|Playwright & Main Execution Pipeline]]
- [[_COMMUNITY_MCP Server Stdio Protocol|MCP Server Stdio Protocol]]
- [[_COMMUNITY_Accessibility Audit Runner|Accessibility Audit Runner]]
- [[_COMMUNITY_Performance Test Runner|Performance Test Runner]]
- [[_COMMUNITY_UX Journey Test Runner|UX Journey Test Runner]]
- [[_COMMUNITY_Test Report Generator|Test Report Generator]]
- [[_COMMUNITY_Demo Website Module|Demo Website Module]]
- [[_COMMUNITY_MCP Server Module|MCP Server Module]]
- [[_COMMUNITY_Testing Scenarios Module|Testing Scenarios Module]]
- [[_COMMUNITY_Community 15|Community 15]]

## God Nodes (most connected - your core abstractions)
1. `JsonValue` - 38 edges
2. `PlaywrightManager` - 23 edges
3. `JsonParser` - 13 edges
4. `DemoWebServer` - 8 edges
5. `PlaywrightToolRegistry` - 6 edges
6. `UxJourneyTestRunner` - 5 edges
7. `AboutHandler` - 4 edges
8. `ContactHandler` - 4 edges
9. `HomeHandler` - 4 edges
10. `LinksHandler` - 4 edges

## Surprising Connections (you probably didn't know these)
- `PlaywrightToolRegistry` --references--> `PlaywrightManager`  [EXTRACTED]
  mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightToolRegistry.java → mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightManager.java

## Import Cycles
- None detected.

## Communities (16 total, 4 thin omitted)

### Community 0 - "JSON Parsing Engine"
Cohesion: 0.07
Nodes (26): Browser, DemoWebServer, Logger, String, AboutHandler, HttpExchange, Override, ContactHandler (+18 more)

### Community 1 - "Demo HTTP Web Server & Handlers"
Cohesion: 0.11
Nodes (10): List, Map, Object, String, SuppressWarnings, JsonValue, Type, String (+2 more)

### Community 2 - "Playwright Automation Manager"
Cohesion: 0.21
Nodes (9): BrowserContext, Logger, Map, Object, Page, String, SuppressWarnings, PlaywrightManager (+1 more)

### Community 3 - "JSON Model & Primitive Types"
Cohesion: 0.33
Nodes (4): Boolean, String, JsonParser, Number

### Community 4 - "Playwright & Main Execution Pipeline"
Cohesion: 0.18
Nodes (7): Logger, McpServer, List, Map, Object, String, PlaywrightToolRegistry

### Community 5 - "MCP Server Stdio Protocol"
Cohesion: 0.31
Nodes (6): AccessibilityTestRunner, Map, Object, Page, String, SuppressWarnings

### Community 6 - "Accessibility Audit Runner"
Cohesion: 0.31
Nodes (6): Map, Object, Page, String, SuppressWarnings, PerformanceTestRunner

### Community 7 - "Performance Test Runner"
Cohesion: 0.42
Nodes (5): Map, Object, Page, String, UxJourneyTestRunner

### Community 8 - "UX Journey Test Runner"
Cohesion: 0.32
Nodes (6): FunctionalTestRunner, Map, Object, Page, String, SuppressWarnings

### Community 9 - "Test Report Generator"
Cohesion: 0.29
Nodes (5): Map, Object, String, SuppressWarnings, TestReportGenerator

## Knowledge Gaps
- **4 isolated node(s):** `demo-website`, `mcp-server`, `com.demo:java-mcp-testing-demo`, `testing-scenarios`
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PlaywrightManager` connect `Playwright Automation Manager` to `JSON Parsing Engine`, `Demo HTTP Web Server & Handlers`, `Playwright & Main Execution Pipeline`?**
  _High betweenness centrality (0.229) - this node is a cross-community bridge._
- **Why does `JsonValue` connect `Demo HTTP Web Server & Handlers` to `Playwright Automation Manager`, `JSON Model & Primitive Types`, `Playwright & Main Execution Pipeline`?**
  _High betweenness centrality (0.219) - this node is a cross-community bridge._
- **What connects `demo-website`, `mcp-server`, `com.demo:java-mcp-testing-demo` to the rest of the system?**
  _4 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `JSON Parsing Engine` be split into smaller, more focused modules?**
  _Cohesion score 0.07246376811594203 - nodes in this community are weakly interconnected._
- **Should `Demo HTTP Web Server & Handlers` be split into smaller, more focused modules?**
  _Cohesion score 0.1111111111111111 - nodes in this community are weakly interconnected._