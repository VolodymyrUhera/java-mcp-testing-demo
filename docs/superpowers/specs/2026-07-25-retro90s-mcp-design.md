# Design Document: `retro90s` MCP Server

## Overview
`retro90s` is a deterministic, framework-free Model Context Protocol (MCP) server written in Java 21. It acts as an enthusiastic 1990s expert, providing answers, comparisons, recommendations, historical breakdowns, and nostalgia about 90s technology, software, pop culture, media, and hardware.

## Goals & Constraints
- **Framework-free**: Pure Java 21 with standard `com.sun.net.httpserver` HTTP + SSE server.
- **Dependencies**: Minimal (`jackson-databind` for JSON-RPC 2.0 processing, JUnit 5 for testing).
- **Transport**: HTTP + Server-Sent Events (SSE) MCP transport.
- **Deterministic Knowledge Base**: 15 local JSON categories loaded into memory.
- **Search Fallback**: Wikipedia REST API & DuckDuckGo Instant Answer API via `java.net.http.HttpClient` when local index misses.
- **Personality**: Enforces 90s retro-computing expert tone using `personality.md`.

---

## Project Structure

```
retro90s-mcp/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── retro90s/
    │   │           └── mcp/
    │   │               ├── Main.java
    │   │               ├── Retro90sServer.java
    │   │               ├── ToolRegistry.java
    │   │               ├── KnowledgeService.java
    │   │               ├── SearchService.java
    │   │               └── ResourceLoader.java
    │   └── resources/
    │       ├── knowledge/
    │       │   ├── technology.json
    │       │   ├── internet.json
    │       │   ├── windows.json
    │       │   ├── linux.json
    │       │   ├── dos.json
    │       │   ├── games.json
    │       │   ├── consoles.json
    │       │   ├── programming.json
    │       │   ├── hardware.json
    │       │   ├── movies.json
    │       │   ├── music.json
    │       │   ├── television.json
    │       │   ├── fashion.json
    │       │   ├── history.json
    │       │   └── brands.json
    │       └── prompts/
    │           └── personality.md
    └── test/
        └── java/
            └── com/
                └── retro90s/
                    └── mcp/
                        ├── KnowledgeServiceTest.java
                        └── ToolRegistryTest.java
```

---

## Technical Specifications

### 1. Build Setup (`pom.xml`)
- Target: Java 21
- Dependencies:
  - `com.fasterxml.jackson.core:jackson-databind:2.17.0`
  - `org.junit.jupiter:junit-jupiter-api:5.10.2` (test scope)
  - `org.junit.jupiter:junit-jupiter-engine:5.10.2` (test scope)

### 2. Transport Layer (`Retro90sServer.java`)
- Uses `com.sun.net.httpserver.HttpServer` listening on port `8080` (or `PORT` env var).
- `GET /sse`:
  - Starts SSE response connection (`Content-Type: text/event-stream`).
  - Sends initial `endpoint` event containing URI `/message?sessionId=<id>`.
- `POST /message`:
  - Receives JSON-RPC 2.0 requests.
  - Dispatches methods: `initialize`, `ping`, `tools/list`, `tools/call`, `resources/list`, `resources/read`, `prompts/list`, `prompts/get`.

### 3. Knowledge Service (`KnowledgeService.java`)
- Loads JSON files from `/knowledge/*.json` resource directory.
- Main Data Model: `KnowledgeItem` (`id`, `title`, `category`, `year`, `manufacturer`, `summary`, `facts`, `related`, `keywords`).
- Indexes:
  - `Map<String, KnowledgeItem> itemsById`
  - `Map<String, List<KnowledgeItem>> itemsByCategory`
  - `Map<Integer, List<KnowledgeItem>> itemsByYear`
- Provides search algorithms: keyword scoring, exact title matching, and year matching.

### 4. Search Service (`SearchService.java`)
- Used when `KnowledgeService` confidence < 0.7 or search returns empty.
- Queries Wikipedia REST API (`https://en.wikipedia.org/api/rest_v1/page/summary/{title}`) and DuckDuckGo API.
- Converts result into structured `KnowledgeItem`.

### 5. Tool Registry (`ToolRegistry.java`)
Implements 9 MCP Tools:
1. `ask90s` (question: string)
2. `compare` (left: string, right: string)
3. `recommend` (category: string)
4. `explain` (topic: string)
5. `trivia` ()
6. `nostalgia` ()
7. `year` (year: int)
8. `website` (name: string)
9. `hardware` (component: string)

### 6. MCP Resources & Prompts
- Resources: `retro90s://timeline`, `retro90s://operating-systems`, `retro90s://consoles`, `retro90s://programming`, `retro90s://internet`.
- Prompt: `personality.md` loaded via `ResourceLoader`.

---

## Spec Self-Review
1. Placeholder scan: All components fully specified.
2. Internal consistency: Transport, knowledge index, tools, search fallback, and POM align.
3. Scope check: Single focused MCP server project.
4. Ambiguity check: Endpoints, protocols, data schemas defined.
