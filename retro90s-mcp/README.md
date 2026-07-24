# Retro 90s MCP Server (`retro90s-mcp`)

A Model Context Protocol (MCP) server providing a nostalgic and historically accurate knowledge base, personality prompt, and specialized search tools covering the 1990s decade (1990–1999).

---

## 🚀 Overview

`retro90s-mcp` connects LLMs to structured historical data and a themed persona from the 1990s. It contains curated datasets across 15 pop-culture, computing, and historical categories, combined with a 90s tech expert persona prompt.

---

## 📁 Knowledge Categories

The server includes 15 distinct JSON knowledge bases located in `src/main/resources/knowledge/`:

| Category | File | Description |
|---|---|---|
| **Technology** | `technology.json` | Handheld PDAs, Zip drives, portable CD audio, DVD formats, and translucent iMacs. |
| **Internet** | `internet.json` | Web browsers, AOL keywords, GeoCities, AltaVista, ICQ instant messaging. |
| **Windows** | `windows.json` | Windows 3.1, Windows 95, Windows 98, Windows NT 4.0, and Internet Explorer. |
| **Linux** | `linux.json` | Early Linux kernel releases, Slackware, Debian, Red Hat, and Tux the Penguin. |
| **DOS** | `dos.json` | MS-DOS 6.22, Norton Commander, QBasic, DOS extenders, and conventional RAM management. |
| **Games** | `games.json` | Iconic FPS, RTS, and 3D action titles like DOOM, Quake, Half-Life, StarCraft, and Zelda OOT. |
| **Consoles** | `consoles.json` | 16-bit to 128-bit hardware including SNES, PlayStation 1, Nintendo 64, Saturn, and Dreamcast. |
| **Programming** | `programming.json` | Emergence of Java, JavaScript, Python 1.0, Delphi, and server-side PHP. |
| **Hardware** | `hardware.json` | 3dfx Voodoo graphics cards, Intel Pentium CPUs, Sound Blaster 16, and alternative CPUs. |
| **Movies** | `movies.json` | 90s cinematic milestones like Jurassic Park, The Matrix, Pulp Fiction, Titanic, and Toy Story. |
| **Music** | `music.json` | Grunge rock, alternative, big beat rave, East Coast hip-hop, and 90s pop phenomena. |
| **Television** | `television.json` | Definitive TV series including The X-Files, Friends, Seinfeld, Twin Peaks, and Pokémon. |
| **Fashion** | `fashion.json` | Grunge flannels, JNCO wide-leg jeans, neon windbreakers, platforms, and snapbacks. |
| **History** | `history.json` | Fall of the Soviet Union, German reunification, Hubble Telescope, Y2K panic, and Dolly the Sheep. |
| **Brands** | `brands.json` | 90s pop culture brands including Tamagotchi, Beanie Babies, Blockbuster Video, Nike Air, and Game Boy. |

---

## 🏗️ Architecture & Schema

Each entry across all knowledge files conforms to a strict JSON schema:

```json
[
  {
    "id": "unique-slug-year",
    "title": "Title of Item",
    "category": "category-name",
    "year": 1995,
    "manufacturer": "Company or Creator",
    "summary": "Concise summary of significance.",
    "facts": [
      "Historically accurate technical or cultural fact 1.",
      "Historically accurate technical or cultural fact 2."
    ],
    "related": [
      "related-item-id-1",
      "related-item-id-2"
    ],
    "keywords": [
      "search-term-1",
      "search-term-2"
    ]
  }
]
```

### Prompt Resource
- **Personality Prompt**: Located at `src/main/resources/prompts/personality.md`, defining the **Cyber-Steve** 90s guru persona.

---

## 💡 Core Components & Services

### `KnowledgeItem` (`com.retro90s.mcp.KnowledgeItem`)
Java 21 record modeling a single retro 90s entry with fields:
- `id` (String): Unique slug identifier.
- `title` (String): Human-readable item title.
- `category` (String): Knowledge domain category.
- `year` (int): Release or historical year (1990–1999).
- `manufacturer` (String): Company, creator, or entity.
- `summary` (String): Concise historical summary.
- `facts` (List<String>): List of verified historical facts.
- `related` (List<String>): Identifiers of related items.
- `keywords` (List<String>): Search tags and aliases.

### `ResourceLoader` (`com.retro90s.mcp.ResourceLoader`)
Loads classpath resources (`/knowledge/*.json` datasets and `/prompts/personality.md`) using Jackson `ObjectMapper`.

### `KnowledgeService` (`com.retro90s.mcp.KnowledgeService`)
In-memory dataset index and search provider offering:
- **Search Query Algorithm**: Multi-tiered search combining exact ID/title/category match, keyword term scoring across all fields, and Levenshtein fuzzy matching for typos.
- **Lookup Methods**: `findById(String id)`, `findByCategory(String category)`, `findByYear(int year)`, and `getRandomItem()`.
- **Resource Presets**: `getTimeline()`, `getOperatingSystems()`, `getConsoles()`, `getProgramming()`, `getInternet()`, and `getPersonalityPrompt()`.

### `SearchService` (`com.retro90s.mcp.SearchService`)
Online search provider with Wikipedia and DuckDuckGo fallback capabilities using Java 21 `HttpClient`:
- **Wikipedia REST API Integration**: Queries `https://en.wikipedia.org/api/rest_v1/page/summary/{query}` for summaries, facts, and metadata.
- **DuckDuckGo Instant Answer Integration**: Fallback queries `https://api.duckduckgo.com/?q={query}&format=json` for abstract text and related topics.
- **Synthesized Knowledge Items**: Converts raw API JSON into fully-typed `KnowledgeItem` records with extracted facts, 1990s year detection, and slug identifiers.
- **Graceful Error Handling**: Handles network dropouts, HTTP 404/500 errors, and malformed responses safely, returning fallback `KnowledgeItem` instances or empty `Optional` objects without throwing unhandled exceptions.


### `ToolRegistry` (`com.retro90s.mcp.ToolRegistry`)
Central MCP tool definitions registry and handler for 9 specialized tools:
- **`listTools()`**: Returns JSON Schema list of all 9 supported MCP tools for `tools/list`.
- **`callTool(String toolName, JsonNode arguments)`**: Executes requested tool using `KnowledgeService` with `SearchService` fallback, returning standard MCP JSON-RPC tool result objects (`{ "content": [ { "type": "text", "text": "..." } ], "isError": false }`).

---

## 🛠️ MCP Tools

`retro90s-mcp` implements 9 Model Context Protocol tools. All tools follow JSON-RPC 2.0 and MCP tool specifications.

| Tool Name | Description | Inputs | Output Schema |
|---|---|---|---|
| **`ask90s`** | Ask Cyber-Steve any question about 90s technology, software, hardware, pop culture, or historical events. | `question` (string, required) | Standard MCP Tool Result object containing Cyber-Steve commentary and historical facts. |
| **`compare`** | Compare two 90s items, software, hardware, or pop culture phenomena. | `left` (string, required), `right` (string, required) | Detailed side-by-side 90s showdown comparison breakdown. |
| **`recommend`** | Get top 90s recommendations for a given category or random picks. | `category` (string, optional) | Curated recommendations list with summaries and release years. |
| **`explain`** | In-depth historical and technical explanation of a 90s concept, technology, or event. | `topic` (string, required) | Deep-dive explanation with creator, year, facts, and related topics. |
| **`trivia`** | Get random 90s trivia question or obscure historical facts. | `category` (string, optional) | Random trivia fact with category and item context. |
| **`nostalgia`** | Generate a nostalgic 90s memory trip, combining retro tech, culture, and Cyber-Steve commentary. | `theme` (string, optional) | Story-based nostalgia trip packed with 90s jargon and retro culture. |
| **`year`** | Get a comprehensive breakdown of major 90s releases and events for a specific year (1990–1999). | `year` (integer, required) | Chronological breakdown of releases and historical events for that year. |
| **`website`** | Explore 90s internet landmarks, early web browsers, search engines, and dot-com sites. | `name` (string, required) | Early web specs, simulated URL, launch year, and web archive notes. |
| **`hardware`** | Get detailed technical specs and history for 90s hardware, CPUs, GPUs, and peripherals. | `component` (string, required) | Hardware spec sheet with manufacturer, year, and technical facts. |

### Tool Execution JSON-RPC Format

#### `tools/list` Request & Response
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/list"
}
```
Response:
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "tools": [
      {
        "name": "ask90s",
        "description": "Ask Cyber-Steve any question about 90s technology...",
        "inputSchema": {
          "type": "object",
          "properties": {
            "question": {
              "type": "string",
              "description": "The 90s question or topic to search and ask about."
            }
          },
          "required": ["question"]
        }
      }
    ]
  }
}
```

#### `tools/call` Request & Response
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/call",
  "params": {
    "name": "ask90s",
    "arguments": {
      "question": "Windows 95"
    }
  }
}
```
Response:
```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "result": {
    "content": [
      {
        "type": "text",
        "text": "🕹️ Cyber-Steve says: Booyah! Here is the lowdown on 'Windows 95'..."
      }
    ],
    "isError": false
  }
}
```

---

## 🛠️ Build & Verification

Requirements:
- Java 21+
- Maven 3.8+

### Compile & Test Project
```bash
mvn clean test -f retro90s-mcp/pom.xml
```

---

## 📜 License

MIT License. Built as part of the Java MCP Testing Demo suite.
