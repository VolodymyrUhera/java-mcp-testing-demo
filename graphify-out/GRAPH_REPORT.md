# Graph Report - /home/voha/Documents/JiraMCP  (2026-07-24)

## Corpus Check
- Corpus is ~33,209 words - fits in a single context window. You may not need a graph.

## Summary
- 186 nodes · 370 edges · 17 communities (12 shown, 5 thin omitted)
- Extraction: 82% EXTRACTED · 18% INFERRED · 0% AMBIGUOUS · INFERRED: 68 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]
- [[_COMMUNITY_Community 6|Community 6]]
- [[_COMMUNITY_Community 7|Community 7]]
- [[_COMMUNITY_Community 8|Community 8]]
- [[_COMMUNITY_Community 9|Community 9]]
- [[_COMMUNITY_Community 12|Community 12]]
- [[_COMMUNITY_Community 13|Community 13]]
- [[_COMMUNITY_Community 14|Community 14]]
- [[_COMMUNITY_Community 15|Community 15]]
- [[_COMMUNITY_Community 16|Community 16]]

## God Nodes (most connected - your core abstractions)
1. `JsonValue` - 38 edges
2. `PlaywrightManager` - 23 edges
3. `JsonParser` - 13 edges
4. `DemoWebServer` - 8 edges
5. `PlaywrightToolRegistry` - 6 edges
6. `UxJourneyTestRunner` - 5 edges
7. `StaticAssetHandler` - 4 edges
8. `McpMessage` - 4 edges
9. `AccessibilityTestRunner` - 4 edges
10. `FunctionalTestRunner` - 4 edges

## Surprising Connections (you probably didn't know these)
- `PlaywrightToolRegistry` --references--> `PlaywrightManager`  [EXTRACTED]
  mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightToolRegistry.java → mcp-server/src/main/java/com/demo/mcp/tools/PlaywrightManager.java

## Import Cycles
- None detected.

## Communities (17 total, 5 thin omitted)

### Community 0 - "Community 0"
Cohesion: 0.08
Nodes (22): DemoWebServer, Logger, String, AboutHandler, HttpExchange, Override, ContactHandler, HttpExchange (+14 more)

### Community 1 - "Community 1"
Cohesion: 0.14
Nodes (7): Boolean, String, JsonParser, Object, JsonValue, Type, Number

### Community 2 - "Community 2"
Cohesion: 0.24
Nodes (8): BrowserContext, Logger, Map, Object, Page, String, SuppressWarnings, PlaywrightManager

### Community 3 - "Community 3"
Cohesion: 0.19
Nodes (7): List, Map, String, SuppressWarnings, String, McpMessage, String

### Community 4 - "Community 4"
Cohesion: 0.16
Nodes (11): Browser, Logger, Playwright, FunctionalTestRunner, Map, Object, Page, String (+3 more)

### Community 5 - "Community 5"
Cohesion: 0.18
Nodes (7): Logger, McpServer, List, Map, Object, String, PlaywrightToolRegistry

### Community 6 - "Community 6"
Cohesion: 0.31
Nodes (6): AccessibilityTestRunner, Map, Object, Page, String, SuppressWarnings

### Community 7 - "Community 7"
Cohesion: 0.31
Nodes (6): Map, Object, Page, String, SuppressWarnings, PerformanceTestRunner

### Community 8 - "Community 8"
Cohesion: 0.42
Nodes (5): Map, Object, Page, String, UxJourneyTestRunner

### Community 9 - "Community 9"
Cohesion: 0.29
Nodes (5): Map, Object, String, SuppressWarnings, TestReportGenerator

## Knowledge Gaps
- **4 isolated node(s):** `demo-website`, `mcp-server`, `com.demo:java-mcp-testing-demo`, `testing-scenarios`
  These have ≤1 connection - possible missing edges or undocumented components.
- **5 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PlaywrightManager` connect `Community 2` to `Community 3`, `Community 4`, `Community 5`?**
  _High betweenness centrality (0.410) - this node is a cross-community bridge._
- **Why does `JsonValue` connect `Community 1` to `Community 2`, `Community 3`, `Community 5`?**
  _High betweenness centrality (0.199) - this node is a cross-community bridge._
- **What connects `demo-website`, `mcp-server`, `com.demo:java-mcp-testing-demo` to the rest of the system?**
  _4 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.08333333333333333 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.14112903225806453 - nodes in this community are weakly interconnected._