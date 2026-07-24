# Graph Report - .  (2026-07-24)

## Corpus Check
- 185 files · ~0 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 185 nodes · 377 edges · 16 communities (12 shown, 4 thin omitted)
- Extraction: 80% EXTRACTED · 20% INFERRED · 0% AMBIGUOUS · INFERRED: 75 edges (avg confidence: 0.8)
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

### Community 0 - "Community 0"
Cohesion: 0.07
Nodes (26): Browser, DemoWebServer, Logger, String, AboutHandler, HttpExchange, Override, ContactHandler (+18 more)

### Community 1 - "Community 1"
Cohesion: 0.11
Nodes (10): List, Map, Object, String, SuppressWarnings, JsonValue, Type, String (+2 more)

### Community 2 - "Community 2"
Cohesion: 0.21
Nodes (9): BrowserContext, Logger, Map, Object, Page, String, SuppressWarnings, PlaywrightManager (+1 more)

### Community 3 - "Community 3"
Cohesion: 0.33
Nodes (4): Boolean, String, JsonParser, Number

### Community 4 - "Community 4"
Cohesion: 0.18
Nodes (7): Logger, McpServer, List, Map, Object, String, PlaywrightToolRegistry

### Community 5 - "Community 5"
Cohesion: 0.31
Nodes (6): AccessibilityTestRunner, Map, Object, Page, String, SuppressWarnings

### Community 6 - "Community 6"
Cohesion: 0.31
Nodes (6): Map, Object, Page, String, SuppressWarnings, PerformanceTestRunner

### Community 7 - "Community 7"
Cohesion: 0.42
Nodes (5): Map, Object, Page, String, UxJourneyTestRunner

### Community 8 - "Community 8"
Cohesion: 0.32
Nodes (6): FunctionalTestRunner, Map, Object, Page, String, SuppressWarnings

### Community 9 - "Community 9"
Cohesion: 0.29
Nodes (5): Map, Object, String, SuppressWarnings, TestReportGenerator

## Knowledge Gaps
- **4 isolated node(s):** `demo-website`, `mcp-server`, `com.demo:java-mcp-testing-demo`, `testing-scenarios`
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `PlaywrightManager` connect `Community 2` to `Community 0`, `Community 1`, `Community 4`?**
  _High betweenness centrality (0.229) - this node is a cross-community bridge._
- **Why does `JsonValue` connect `Community 1` to `Community 2`, `Community 3`, `Community 4`?**
  _High betweenness centrality (0.219) - this node is a cross-community bridge._
- **What connects `demo-website`, `mcp-server`, `com.demo:java-mcp-testing-demo` to the rest of the system?**
  _4 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.07246376811594203 - nodes in this community are weakly interconnected._
- **Should `Community 1` be split into smaller, more focused modules?**
  _Cohesion score 0.1111111111111111 - nodes in this community are weakly interconnected._