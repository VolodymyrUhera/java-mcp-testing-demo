# Retro 90s MCP Server (`retro90s-mcp`) 🕹️⚡

A Model Context Protocol (MCP) server providing a nostalgic and historically accurate knowledge base, personality prompt, resources, and specialized search tools covering the 1990s decade (1990–1999).

Built in pure Java 21 using `com.sun.net.httpserver.HttpServer` with Server-Sent Events (SSE) and JSON-RPC 2.0 over HTTP.

---

## 🚀 Overview

`retro90s-mcp` connects LLMs to structured historical data and a themed persona from the 1990s. It contains curated datasets across 15 pop-culture, computing, and historical categories, combined with a 90s tech expert persona prompt.

---

## 🏛️ Architecture Overview

The server operates completely framework-free using Java 21 Standard Library and Jackson JSON parsing:

```
                  +-----------------------------------+
                  |            MCP Client             |
                  +-----------------------------------+
                               |         ^
                       GET /sse|         | JSON-RPC 2.0 Response
                               v         | POST /message
                  +-----------------------------------+
                  |          Retro90sServer           |
                  |  (HttpServer on port 8080/PORT)   |
                  +-----------------------------------+
                               |                 |
                               v                 v
                    +--------------------+  +--------------------+
                    |    ToolRegistry    |  |  Resource Loader   |
                    +--------------------+  +--------------------+
                               |                     |
                               v                     v
                    +--------------------+  +--------------------+
                    |  KnowledgeService  |  |  prompts/          |
                    | (15 JSON Datasets) |  |  personality.md    |
                    +--------------------+  +--------------------+
                               |
                        fallback search
                               v
                    +--------------------+
                    |   SearchService    |
                    | (Wikipedia REST /  |
                    |  DuckDuckGo API)   |
                    +--------------------+
```

### Core Components
1. **`Main` (`com.retro90s.mcp.Main`)**: Application entrypoint initializing services, resolving environment configuration (`PORT`), and starting `Retro90sServer`.
2. **`Retro90sServer` (`com.retro90s.mcp.Retro90sServer`)**: Embedded HTTP SSE server processing `/sse` GET connections and `/message` POST JSON-RPC 2.0 requests.
3. **`ToolRegistry` (`com.retro90s.mcp.ToolRegistry`)**: Manages tool schemas (`tools/list`) and executes all 9 specialized retro MCP tools (`tools/call`).
4. **`KnowledgeService` (`com.retro90s.mcp.KnowledgeService`)**: In-memory dataset index supporting multi-tiered search (exact, keyword scoring, Levenshtein fuzzy matching) and pre-filtered resource views.
5. **`SearchService` (`com.retro90s.mcp.SearchService`)**: Online search provider using Java 21 `HttpClient` querying Wikipedia REST & DuckDuckGo APIs when local confidence is insufficient.
6. **`ResourceLoader` (`com.retro90s.mcp.ResourceLoader`)**: Classpath reader for 15 JSON knowledge files and `personality.md`.

---

## 📡 HTTP SSE & JSON-RPC Connection Details

### 1. Establish SSE Connection
- **Endpoint**: `GET /sse`
- **Headers**:
  - `Accept: text/event-stream`
- **Behavior**: Emits an initial `endpoint` SSE event pointing to the session-specific `/message` POST target URI:
  ```
  event: endpoint
  data: /message?sessionId=123e4567-e89b-12d3-a456-426614174000
  ```

### 2. Send JSON-RPC Requests
- **Endpoint**: `POST /message` (or `POST /message?sessionId=<uuid>`)
- **Headers**:
  - `Content-Type: application/json`
- **Supported Methods**:
  - `initialize`: Handshake returning protocol version (`2024-11-05`), capabilities, and server info.
  - `ping`: Liveness check.
  - `tools/list`: Returns list of all 9 retro MCP tool definitions.
  - `tools/call`: Executes a specific tool with arguments.
  - `resources/list`: Returns available resource URIs (`retro90s://timeline`, `retro90s://operating-systems`, `retro90s://consoles`, `retro90s://programming`, `retro90s://internet`).
  - `resources/read`: Reads JSON dataset for a given resource URI.
  - `prompts/list`: Returns prompt definitions (`personality`).
  - `prompts/get`: Gets prompt content from `personality.md`.

---

## 📁 Knowledge Categories & Resources

### 15 Datasets (`src/main/resources/knowledge/*.json`)
- **`technology.json`**: Handheld PDAs, Zip drives, portable CD audio, DVD formats, translucent iMacs.
- **`internet.json`**: Web browsers, AOL keywords, GeoCities, AltaVista, ICQ instant messaging.
- **`windows.json`**: Windows 3.1, Windows 95, Windows 98, Windows NT 4.0, Internet Explorer.
- **`linux.json`**: Early Linux kernel releases, Slackware, Debian, Red Hat, Tux the Penguin.
- **`dos.json`**: MS-DOS 6.22, Norton Commander, QBasic, conventional RAM management.
- **`games.json`**: DOOM, Quake, Half-Life, StarCraft, Zelda OOT.
- **`consoles.json`**: SNES, Genesis, PlayStation 1, Nintendo 64, Saturn, Dreamcast.
- **`programming.json`**: Java, JavaScript, Python 1.0, Delphi, Visual Basic, Perl, PHP.
- **`hardware.json`**: 3dfx Voodoo graphics cards, Intel Pentium CPUs, Sound Blaster 16.
- **`movies.json`**: Jurassic Park, The Matrix, Pulp Fiction, Titanic, Toy Story.
- **`music.json`**: Grunge rock, alternative, big beat rave, East Coast hip-hop, 90s pop.
- **`television.json`**: The X-Files, Friends, Seinfeld, Twin Peaks, Pokémon.
- **`fashion.json`**: Grunge flannels, JNCO jeans, neon windbreakers, platforms, snapbacks.
- **`history.json`**: Fall of Soviet Union, German reunification, Hubble Telescope, Y2K panic.
- **`brands.json`**: Tamagotchi, Beanie Babies, Blockbuster Video, Nike Air, Game Boy.

### Resource URIs
- `retro90s://timeline`: Chronological index of 1990s technology, software, hardware, and culture events.
- `retro90s://operating-systems`: Index of 90s OS releases (Windows, Linux, DOS).
- `retro90s://consoles`: Index of 90s video game console hardware.
- `retro90s://programming`: Index of 90s programming languages and development tools.
- `retro90s://internet`: Index of early web portals, dot-com sites, and internet history.

---

## 🛠️ MCP Tools

| Tool Name | Description | Inputs |
|---|---|---|
| **`ask90s`** | Ask Cyber-Steve any question about 90s technology, pop culture, or historical events. | `question` (string, required) |
| **`compare`** | Compare two 90s items, software, hardware, or pop culture phenomena. | `left` (string, required), `right` (string, required) |
| **`recommend`** | Get top 90s recommendations for a given category or random picks. | `category` (string, optional) |
| **`explain`** | In-depth historical and technical explanation of a 90s concept, technology, or event. | `topic` (string, required) |
| **`trivia`** | Get random 90s trivia question or obscure historical facts. | `category` (string, optional) |
| **`nostalgia`** | Generate a nostalgic 90s memory trip with retro tech, culture, and Cyber-Steve commentary. | `theme` (string, optional) |
| **`year`** | Get a comprehensive breakdown of major 90s releases and events for a year (1990–1999). | `year` (integer, required) |
| **`website`** | Explore 90s internet landmarks, early web browsers, search engines, and dot-com sites. | `name` (string, required) |
| **`hardware`** | Get detailed technical specs and history for 90s hardware, CPUs, GPUs, and peripherals. | `component` (string, required) |

---

## 💻 Build & Running Instructions

### Requirements
- **Java 21+** (JDK 21 LTS)
- **Maven 3.8+**

### Compile & Package
```bash
mvn clean package -f retro90s-mcp/pom.xml
```

### Run Server
```bash
# Run using java -jar
java -jar retro90s-mcp/target/retro90s-mcp-1.0.0-SNAPSHOT.jar

# Or run on custom port
PORT=9090 java -cp retro90s-mcp/target/retro90s-mcp-1.0.0-SNAPSHOT.jar com.retro90s.mcp.Main
```

---

## 🧪 `curl` Request Examples

### 1. Connect to SSE Stream
```bash
curl -N http://localhost:8080/sse
```
*Output:*
```
event: endpoint
data: /message?sessionId=4e8b39c0-9d41-4b74-a6bf-8016d9ad22e1
```

### 2. Protocol Handshake (`initialize`)
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "initialize"
  }'
```

### 3. List Available Tools (`tools/list`)
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "tools/list"
  }'
```

### 4. Call `ask90s` Tool (`tools/call`)
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 3,
    "method": "tools/call",
    "params": {
      "name": "ask90s",
      "arguments": {
        "question": "What was Windows 95?"
      }
    }
  }'
```

### 5. List Resources (`resources/list`)
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 4,
    "method": "resources/list"
  }'
```

### 6. Read Timeline Resource (`resources/read`)
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 5,
    "method": "resources/read",
    "params": {
      "uri": "retro90s://timeline"
    }
  }'
```

### 7. Get Personality Prompt (`prompts/get`)
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 6,
    "method": "prompts/get",
    "params": {
      "name": "personality"
    }
  }'
```

---

## 📜 License

MIT License. Built as part of the Java MCP Testing Demo suite.
