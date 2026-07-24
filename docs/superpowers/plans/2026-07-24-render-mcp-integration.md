# Render MCP Integration & App Deployment Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Integrate the Render MCP server into `.mcp.json` and prepare the application for seamless cloud deployment to Render using Docker and Render Blueprints (`render.yaml`).

**Architecture:** Add the `render` server entry in `.mcp.json` using `@niyogi/render-mcp`, update `DemoWebServer.java` to support dynamic `PORT` environment variables, create a multi-stage `Dockerfile` and `render.yaml` IaC specification, and document the complete deployment workflow in `docs/deployment.md` and `README.md`.

**Tech Stack:** Java 21 (`HttpServer`), Maven 3.9, Docker, Render Blueprints (`render.yaml`), `@niyogi/render-mcp` (Node.js/npx).

## Global Constraints

- Java 21 compatibility.
- Zero extra runtime dependencies in `demo-website` (standard library only).
- Keep `.mcp.json` valid JSON syntax.

---

### Task 1: Add Cloud-Native `PORT` Environment Variable Support in `DemoWebServer.java`

**Files:**
- Modify: `demo-website/src/main/java/com/demo/website/DemoWebServer.java:50-62`

**Interfaces:**
- Consumes: `System.getenv("PORT")`
- Produces: `DemoWebServer(int port)` initialized with environment `$PORT` when present.

- [ ] **Step 1: Inspect and update main method port resolution in `DemoWebServer.java`**

Update `DemoWebServer.java` `main` method:
```java
    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            try {
                port = Integer.parseInt(envPort.trim());
            } catch (NumberFormatException ignored) {}
        } else if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        DemoWebServer webServer = new DemoWebServer(port);
        webServer.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(webServer::stop));
    }
```

- [ ] **Step 2: Build project and verify compilation**

Run: `mvn clean package -pl demo-website -DskipTests`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Test port resolution logic via command line**

Run: `mvn package -DskipTests && PORT=9090 java -jar demo-website/target/demo-website-1.0.0-SNAPSHOT.jar &`
Wait 2 seconds, check `curl -I http://localhost:9090/` returns HTTP 200, then kill the process.

- [ ] **Step 4: Commit changes**

```bash
git add demo-website/src/main/java/com/demo/website/DemoWebServer.java
git commit -m "feat(demo-website): support PORT environment variable for cloud deployment"
```

---

### Task 2: Create Containerization Config (`Dockerfile` & `.dockerignore`)

**Files:**
- Create: `Dockerfile`
- Create: `.dockerignore`

**Interfaces:**
- Consumes: Maven source tree, `pom.xml`, Java 21 JRE
- Produces: Runnable Docker container running `demo-website` on `$PORT` (default 8080).

- [ ] **Step 1: Create `.dockerignore`**

```gitignore
.git
.github
target
mcp-server/target
demo-website/target
testing-scenarios/target
reports
graphify-out
.superpowers
```

- [ ] **Step 2: Create multi-stage `Dockerfile`**

```dockerfile
# Stage 1: Build application using Maven & Temurin JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY demo-website demo-website
COPY mcp-server mcp-server
COPY testing-scenarios testing-scenarios
RUN mvn clean package -DskipTests

# Stage 2: Minimal JRE 21 Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/demo-website/target/demo-website-1.0.0-SNAPSHOT.jar /app/demo-website.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "demo-website.jar"]
```

- [ ] **Step 3: Commit Docker configuration**

```bash
git add Dockerfile .dockerignore
git commit -m "feat(docker): add multi-stage Dockerfile for demo-website containerization"
```

---

### Task 3: Configure Render Blueprint (`render.yaml`) & MCP Server (`.mcp.json`)

**Files:**
- Create: `render.yaml`
- Modify: `.mcp.json`

**Interfaces:**
- Consumes: Render API / Blueprint schema, `@niyogi/render-mcp` package
- Produces: Declarative infrastructure blueprint for Render web service and registered `render` MCP server.

- [ ] **Step 1: Create `render.yaml` Blueprint file**

```yaml
services:
  - type: web
    name: jira-mcp-demo-website
    env: docker
    plan: free
    healthCheckPath: /
    autoDeploy: true
    envVars:
      - key: PORT
        value: 8080
```

- [ ] **Step 2: Update `.mcp.json` to register Render MCP server**

Update `.mcp.json`:
```json
{
  "mcpServers": {
    "java-playwright-mcp": {
      "command": "java",
      "args": [
        "-jar",
        "/home/voha/Documents/JiraMCP/mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar"
      ]
    },
    "render": {
      "command": "npx",
      "args": [
        "-y",
        "@niyogi/render-mcp",
        "start"
      ],
      "env": {
        "RENDER_API_KEY": "${RENDER_API_KEY}"
      }
    }
  }
}
```

- [ ] **Step 3: Commit Render blueprint and MCP configuration**

```bash
git add render.yaml .mcp.json
git commit -m "feat(mcp): register Render MCP server in .mcp.json and add render.yaml blueprint"
```

---

### Task 4: Update Documentation (`docs/deployment.md` & `README.md`)

**Files:**
- Modify: `docs/deployment.md`
- Modify: `README.md`

**Interfaces:**
- Consumes: Project documentation structure
- Produces: Clear, step-by-step guidance on setting up `RENDER_API_KEY`, using Render MCP server tools, and deploying to Render via Blueprint or Dashboard.

- [ ] **Step 1: Add Render Deployment & Render MCP section to `docs/deployment.md`**

Append comprehensive Render cloud deployment section to `docs/deployment.md` including:
- Overview of Render Cloud deployment.
- Using `render.yaml` and `Dockerfile`.
- Configuring `RENDER_API_KEY` in environment.
- Using Render MCP tools (`render` MCP server) to deploy, monitor, view logs, and manage services.

- [ ] **Step 2: Update `README.md` with Render references**

Update `README.md` features table and execution section to highlight Render deployment and Render MCP integration.

- [ ] **Step 3: Commit documentation updates**

```bash
git add docs/deployment.md README.md
git commit -m "docs: add Render cloud deployment guide and Render MCP server integration documentation"
```
