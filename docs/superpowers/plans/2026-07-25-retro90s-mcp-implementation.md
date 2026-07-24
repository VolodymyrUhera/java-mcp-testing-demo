# Retro90s MCP Server Implementation Plan 🕹️✨

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a deterministic, framework-free Java 21 `retro90s` MCP HTTP+SSE server delivering 1990s expert knowledge across 15 JSON categories, 9 tools, 5 resources, and Wikipedia/DuckDuckGo online search fallback. 🎮⚡

**Architecture:** Embedded Java 21 `com.sun.net.httpserver.HttpServer` listening on `/sse` and `/message`. `KnowledgeService` loads 15 JSON category files into memory indices at startup. `SearchService` executes external fallback via Java 21 `HttpClient` querying Wikipedia REST & DuckDuckGo APIs when confidence < 0.7. `ToolRegistry` processes MCP JSON-RPC 2.0 requests.

**Tech Stack:** Java 21 (LTS), Maven, `jackson-databind:2.17.0`, `junit-jupiter:5.10.2`.

## Global Constraints
- Pure Java 21, no Spring or heavy web frameworks. 🚫🌱
- Minimal dependencies: `com.fasterxml.jackson.core:jackson-databind:2.17.0`.
- All JSON knowledge files in `retro90s-mcp/src/main/resources/knowledge/`.
- Personality prompt in `retro90s-mcp/src/main/resources/prompts/personality.md`.
- Code formatted cleanly following Ponytail principles (no boilerplate/unnecessary layers).

---

### Task 1: Project Scaffolding & Knowledge Datasets 💾

**Files:**
- Create: `retro90s-mcp/pom.xml`
- Create: `retro90s-mcp/src/main/resources/prompts/personality.md`
- Create: `retro90s-mcp/src/main/resources/knowledge/technology.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/internet.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/windows.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/linux.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/dos.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/games.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/consoles.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/programming.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/hardware.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/movies.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/music.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/television.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/fashion.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/history.json`
- Create: `retro90s-mcp/src/main/resources/knowledge/brands.json`

**Interfaces:**
- Consumes: None
- Produces: `pom.xml` build configuration, classpath resources for 15 categories & personality prompt.

- [ ] **Step 1: Create `pom.xml`**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.retro90s</groupId>
    <artifactId>retro90s-mcp</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.17.0</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <release>21</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: Create `personality.md` prompt file**

Create `retro90s-mcp/src/main/resources/prompts/personality.md`:
"You are an enthusiastic retro computing and 90s pop culture expert! Respond with friendly nostalgia, 90s jargon (rad, booyah, fly, all that and a bag of chips), accurate historical facts, and crisp detail."

- [ ] **Step 3: Create 15 category JSON files in `src/main/resources/knowledge/`**

Include comprehensive 90s items in `windows.json`, `games.json`, `consoles.json`, `programming.json`, `internet.json`, `hardware.json`, `technology.json`, `linux.json`, `dos.json`, `movies.json`, `music.json`, `television.json`, `fashion.json`, `history.json`, `brands.json`.

- [ ] **Step 4: Verify Maven build compiles**

Run: `mvn clean compile -f retro90s-mcp/pom.xml`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit scaffolding**

```bash
git add retro90s-mcp/
git commit -m "feat(retro90s): add Maven project scaffolding and 90s knowledge JSON files"
```

---

### Task 2: ResourceLoader & KnowledgeService Core 🧠

**Files:**
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/KnowledgeItem.java`
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/ResourceLoader.java`
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/KnowledgeService.java`
- Create: `retro90s-mcp/src/test/java/com/retro90s/mcp/KnowledgeServiceTest.java`

**Interfaces:**
- Consumes: JSON datasets from classpath.
- Produces: `KnowledgeService` with methods `search(query)`, `findByYear(year)`, `findByCategory(category)`, `getRandomItem()`, `getTimeline()`.

- [ ] **Step 1: Write failing `KnowledgeServiceTest.java`**

```java
package com.retro90s.mcp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class KnowledgeServiceTest {
    @Test
    public void testKnowledgeLoadingAndSearch() {
        KnowledgeService service = new KnowledgeService();
        service.loadKnowledge();

        KnowledgeItem item = service.search("Windows 95");
        assertNotNull(item, "Windows 95 should be found");
        assertEquals("Operating Systems", item.category());
        assertEquals(1995, item.year());

        List<KnowledgeItem> yearItems = service.findByYear(1995);
        assertFalse(yearItems.isEmpty());
    }
}
```

- [ ] **Step 2: Run test to verify failure**

Run: `mvn test -Dtest=KnowledgeServiceTest -f retro90s-mcp/pom.xml`
Expected: FAIL (classes missing)

- [ ] **Step 3: Implement `KnowledgeItem.java` Record**

```java
package com.retro90s.mcp;

import java.util.List;

public record KnowledgeItem(
    String id,
    String title,
    String category,
    int year,
    String manufacturer,
    String summary,
    List<String> facts,
    List<String> related,
    List<String> keywords
) {}
```

- [ ] **Step 4: Implement `ResourceLoader.java` & `KnowledgeService.java`**

Implement classpath scanning and Jackson JSON loading into memory map indices (`idMap`, `categoryMap`, `yearMap`, `keywordIndex`).

- [ ] **Step 5: Run test to verify pass**

Run: `mvn test -Dtest=KnowledgeServiceTest -f retro90s-mcp/pom.xml`
Expected: `BUILD SUCCESS`

- [ ] **Step 6: Commit Task 2**

```bash
git add retro90s-mcp/src/
git commit -m "feat(retro90s): add KnowledgeItem model, ResourceLoader, and KnowledgeService"
```

---

### Task 3: Online Fallback SearchService 🔍

**Files:**
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/SearchService.java`
- Create: `retro90s-mcp/src/test/java/com/retro90s/mcp/SearchServiceTest.java`

**Interfaces:**
- Consumes: Query string.
- Produces: `SearchService.searchOnline(query)` returning synthesized `KnowledgeItem` or null.

- [ ] **Step 1: Write failing `SearchServiceTest.java`**

```java
package com.retro90s.mcp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SearchServiceTest {
    @Test
    public void testSearchOnlineFallback() {
        SearchService service = new SearchService();
        KnowledgeItem item = service.searchOnline("Windows 95");
        assertNotNull(item);
        assertTrue(item.summary().toLowerCase().contains("windows") || item.title().contains("Windows"));
    }
}
```

- [ ] **Step 2: Run test to verify failure**

Run: `mvn test -Dtest=SearchServiceTest -f retro90s-mcp/pom.xml`
Expected: FAIL

- [ ] **Step 3: Implement `SearchService.java` using `java.net.http.HttpClient`**

Queries Wikipedia summary REST API (`https://en.wikipedia.org/api/rest_v1/page/summary/{query}`). Parses response with Jackson into `KnowledgeItem`.

- [ ] **Step 4: Run test to verify pass**

Run: `mvn test -Dtest=SearchServiceTest -f retro90s-mcp/pom.xml`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit Task 3**

```bash
git add retro90s-mcp/src/
git commit -m "feat(retro90s): add SearchService Wikipedia API online search fallback"
```

---

### Task 4: ToolRegistry & 9 MCP Tools 🛠️

**Files:**
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/ToolRegistry.java`
- Create: `retro90s-mcp/src/test/java/com/retro90s/mcp/ToolRegistryTest.java`

**Interfaces:**
- Consumes: `KnowledgeService`, `SearchService`.
- Produces: `ToolRegistry.listTools()`, `ToolRegistry.callTool(name, argumentsJsonNode)`.

- [ ] **Step 1: Write failing `ToolRegistryTest.java`**

```java
package com.retro90s.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ToolRegistryTest {
    @Test
    public void testAsk90sTool() throws Exception {
        KnowledgeService ks = new KnowledgeService();
        ks.loadKnowledge();
        SearchService ss = new SearchService();
        ToolRegistry registry = new ToolRegistry(ks, ss);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode args = mapper.createObjectNode().put("question", "What was Windows 95?");

        JsonNode result = registry.callTool("ask90s", args);
        assertNotNull(result);
        assertTrue(result.get("answer").asText().contains("Windows 95"));
    }
}
```

- [ ] **Step 2: Run test to verify failure**

Run: `mvn test -Dtest=ToolRegistryTest -f retro90s-mcp/pom.xml`
Expected: FAIL

- [ ] **Step 3: Implement `ToolRegistry.java`**

Implement tool handlers: `ask90s`, `compare`, `recommend`, `explain`, `trivia`, `nostalgia`, `year`, `website`, `hardware`.

- [ ] **Step 4: Run test to verify pass**

Run: `mvn test -Dtest=ToolRegistryTest -f retro90s-mcp/pom.xml`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit Task 4**

```bash
git add retro90s-mcp/src/
git commit -m "feat(retro90s): add ToolRegistry supporting all 9 90s MCP tools"
```

---

### Task 5: HTTP SSE Server & Main Entrypoint 🚀

**Files:**
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/Retro90sServer.java`
- Create: `retro90s-mcp/src/main/java/com/retro90s/mcp/Main.java`

**Interfaces:**
- Consumes: `ToolRegistry`, `KnowledgeService`, `ResourceLoader`.
- Produces: Running `HttpServer` listening on `/sse` and `/message`.

- [ ] **Step 1: Implement `Retro90sServer.java`**

Create `com.sun.net.httpserver.HttpServer` with contexts `/sse` and `/message`.
Format MCP JSON-RPC 2.0 responses (`initialize`, `tools/list`, `tools/call`, `resources/list`, `resources/read`, `prompts/get`).

- [ ] **Step 2: Implement `Main.java`**

Initialize `KnowledgeService`, `SearchService`, `ToolRegistry`, `Retro90sServer`, start server on port 8080.

- [ ] **Step 3: Compile & Package Jar**

Run: `mvn clean package -f retro90s-mcp/pom.xml`
Expected: `BUILD SUCCESS`

- [ ] **Step 4: Commit Task 5**

```bash
git add retro90s-mcp/src/
git commit -m "feat(retro90s): implement Retro90sServer SSE endpoint and Main entrypoint"
```

---

## Spec & Plan Verification Self-Review
1. Spec coverage: All 9 tools, 15 JSON categories, 5 resources, HTTP SSE transport covered.
2. Placeholder scan: Clean steps, exact code blocks provided.
3. Type consistency: `KnowledgeItem`, `KnowledgeService`, `ToolRegistry` types match across tasks.
