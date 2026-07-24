# Retro 90s MCP Server (`retro90s-mcp`) 🕹️⚡

A deterministic, lightweight, framework-free Model Context Protocol (MCP) server providing expert knowledge, historical citations, retro pop-culture data, personality prompts, resources, and 9 specialized tools focused on everything related to the **1990s decade (1990–1999)**.

Built in pure Java 21 LTS using standard `com.sun.net.httpserver.HttpServer` with Server-Sent Events (SSE) and JSON-RPC 2.0 over HTTP.

---

## 🌟 Key Features

* **Framework-Free**: Built with pure Java 21 Standard Library (`com.sun.net.httpserver`). Zero heavy frameworks (No Spring, no Netty).
* **Minimal Dependencies**: Uses only `jackson-databind:2.17.0` for JSON-RPC parsing and `junit-jupiter:5.10.2` for testing.
* **Deterministic & Offline-First**: 15 offline JSON knowledge bases loaded into indexed memory maps at startup.
* **Smart Search Fallback**: Automatically queries Wikipedia REST API and DuckDuckGo API via Java 21 `java.net.http.HttpClient` when local confidence is below threshold.
* **Authentic 90s Persona**: Includes "Cyber-Steve" personality prompt system (`personality.md`) for energetic, nostalgic, yet historically accurate responses.
* **MCP Protocol Compatible**: Supports full MCP HTTP SSE connection flow, JSON-RPC 2.0 tools, resources, and prompts.

---

## 🏛️ Architecture Overview

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

### Core Source Code Map (`com.retro90s.mcp`)

| Class | Description |
|---|---|
| [`Main.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/Main.java) | Application entrypoint. Resolves `PORT` env variable, initializes services, starts server, and registers JVM shutdown hooks. |
| [`Retro90sServer.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/Retro90sServer.java) | HTTP server handling `GET /sse` streams and `POST /message` JSON-RPC 2.0 requests (`initialize`, `ping`, `tools/*`, `resources/*`, `prompts/*`). |
| [`ToolRegistry.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/ToolRegistry.java) | Configures schemas for `tools/list` and executes all 9 retro tools in `tools/call`. |
| [`KnowledgeService.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/KnowledgeService.java) | In-memory dataset index supporting multi-tiered search (exact title, keyword scoring, Levenshtein fuzzy match) and pre-filtered resource views. |
| [`SearchService.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/SearchService.java) | Fallback web search provider querying Wikipedia REST & DuckDuckGo APIs using Java 21 `HttpClient`. |
| [`ResourceLoader.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/ResourceLoader.java) | Classpath loader for 15 JSON datasets and `personality.md`. |
| [`KnowledgeItem.java`](file:///home/voha/Documents/JiraMCP/retro90s-mcp/src/main/java/com/retro90s/mcp/KnowledgeItem.java) | Java 21 `record` model (`id`, `title`, `category`, `year`, `manufacturer`, `summary`, `facts`, `related`, `keywords`). |

---

## 📡 HTTP SSE & JSON-RPC Connection Details

### 1. Establish SSE Connection (`GET /sse`)
- **Endpoint**: `GET /sse`
- **Headers**: `Accept: text/event-stream`
- **Behavior**: Emits initial `endpoint` SSE event pointing to the session `/message` POST target:
  ```http
  HTTP/1.1 200 OK
  Content-Type: text/event-stream; charset=UTF-8

  event: endpoint
  data: /message?sessionId=123e4567-e89b-12d3-a456-426614174000
  ```

### 2. Send JSON-RPC Requests (`POST /message`)
- **Endpoint**: `POST /message?sessionId=<uuid>` (or `POST /message`)
- **Headers**: `Content-Type: application/json`

#### Handshake (`initialize`)
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize"
}
```
*Response:*
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "protocolVersion": "2024-11-05",
    "capabilities": {
      "tools": {},
      "resources": {},
      "prompts": {}
    },
    "serverInfo": {
      "name": "retro90s-mcp",
      "version": "1.0.0"
    }
  }
}
```

---

## 🛠️ MCP Tools Reference (9 Tools)

### 1. `ask90s`
General 90s question answering tool.
* **Input**: `{"question": "What was Windows 95?"}`
* **Output**:
  ```json
  {
    "answer": "Windows 95 was Microsoft's major consumer OS introducing the Start menu and taskbar...",
    "category": "Operating Systems",
    "year": 1995,
    "confidence": 0.98
  }
  ```

### 2. `compare`
Compare two 90s items, software, hardware, or games.
* **Input**: `{"left": "Windows 95", "right": "Windows 98"}`
* **Output**: Detailed comparison across Release, Hardware, Networking, Gaming, Stability, Legacy.

### 3. `recommend`
Get top 90s recommendations for a category.
* **Input**: `{"category": "games"}`
* **Output**: Recommendations (DOOM, Quake, StarCraft, Half-Life, Zelda OOT).

### 4. `explain`
In-depth technical and historical explanation.
* **Input**: `{"topic": "Dial-up Internet"}`
* **Output**: Overview, timeline, interesting facts, related technologies.

### 5. `trivia`
Get random 90s trivia facts.
* **Input**: `{}` or `{"category": "hardware"}`
* **Output**: Obscure 90s facts & trivia.

### 6. `nostalgia`
Generate a nostalgic 90s activity list.
* **Input**: `{}` or `{"theme": "bored"}`
* **Output**: Suggested 90s activities (Winamp, GeoCities, Nirvana, Doom).

### 7. `year`
Get major events across categories for a year (1990–1999).
* **Input**: `{"year": 1996}`
* **Output**: Events in technology, movies, music, games, history.

### 8. `website`
Explore 90s internet landmarks and search engines.
* **Input**: `{"name": "Yahoo"}`
* **Output**: 90s history of Yahoo!, Netscape, GeoCities, AOL, ICQ.

### 9. `hardware`
Get specifications and legacy for 90s PC hardware.
* **Input**: `{"component": "Sound Blaster 16"}`
* **Output**: Specs, release year, manufacturer, popularity, legacy.

---

## 📁 Knowledge Categories & Resources

### 15 Datasets (`src/main/resources/knowledge/*.json`)
1. **`technology.json`**: PalmPilot, Zip Drives, DVD-Video, Discman, iMac G3.
2. **`internet.json`**: Dial-up Internet, Netscape Navigator, GeoCities, AOL, ICQ.
3. **`windows.json`**: Windows 3.1, Windows 95, Windows 98, Windows NT 4.0.
4. **`linux.json`**: Linux Kernel 1.0, Slackware, Debian, Red Hat Linux, Tux.
5. **`dos.json`**: MS-DOS 6.22, Norton Commander, QBasic 1.1, Config.sys & Autoexec.bat.
6. **`games.json`**: DOOM, Quake, Half-Life, StarCraft, Zelda Ocarina of Time.
7. **`consoles.json`**: Super Nintendo, Sega Genesis, PlayStation, Nintendo 64, Dreamcast.
8. **`programming.json`**: Java, JavaScript, Python 1.0, Delphi, Visual Basic, Perl, PHP.
9. **`hardware.json`**: 3dfx Voodoo Graphics, Intel Pentium CPU, Sound Blaster 16.
10. **`movies.json`**: Jurassic Park, The Matrix, Pulp Fiction, Titanic, Toy Story.
11. **`music.json`**: Nirvana, Daft Punk, The Notorious B.I.G., Pearl Jam, Prodigy.
12. **`television.json`**: The X-Files, Friends, Seinfeld, Twin Peaks, Pokémon.
13. **`fashion.json`**: Flannel Shirts, JNCO Jeans, Neon Windbreakers, Platform Shoes.
14. **`history.json`**: Fall of Berlin Wall, German Reunification, Hubble Telescope, Y2K Bug.
15. **`brands.json`**: Tamagotchi, Beanie Babies, Blockbuster Video, Nike Air Max 90.

### 5 MCP Resource URIs
- `retro90s://timeline`: Chronological index of 1990–1999 events.
- `retro90s://operating-systems`: Index of 90s operating systems.
- `retro90s://consoles`: Index of 90s game consoles.
- `retro90s://programming`: Index of 90s programming languages.
- `retro90s://internet`: Index of 90s internet history.

---

## 💻 Build & Run Instructions

```bash
# Compile and run unit & integration tests (33 tests)
mvn clean test -f retro90s-mcp/pom.xml

# Package executable JAR
mvn clean package -f retro90s-mcp/pom.xml

# Run server on port 8080
java -jar retro90s-mcp/target/retro90s-mcp-1.0.0-SNAPSHOT.jar

# Or run on custom port
PORT=9090 java -jar retro90s-mcp/target/retro90s-mcp-1.0.0-SNAPSHOT.jar
```

---

## 🧪 `curl` Request Examples

### Connect SSE
```bash
curl -N http://localhost:8080/sse
```

### Call Tool `ask90s`
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "tools/call",
    "params": {
      "name": "ask90s",
      "arguments": {
        "question": "What was Windows 95?"
      }
    }
  }'
```

### Read Resource `retro90s://timeline`
```bash
curl -X POST http://localhost:8080/message \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 2,
    "method": "resources/read",
    "params": {
      "uri": "retro90s://timeline"
    }
  }'
```

---

## 📜 License

MIT License. Built as part of the Java MCP Testing Demo suite.
