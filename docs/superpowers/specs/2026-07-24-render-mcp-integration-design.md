# Render MCP Server Integration & App Deployment Design

- **Date:** 2026-07-24
- **Status:** Approved
- **Target Component:** Project-level MCP configuration (`.mcp.json`), Demo Web Server (`demo-website`), Docker & Render Blueprint configuration (`Dockerfile`, `render.yaml`), and Documentation (`docs/deployment.md`, `README.md`).

---

## 1. Overview & Objectives

Integrate the **Render MCP Server** into the repository to enable AI agents and MCP clients to manage services, monitor build status, trigger redeployments, and view application logs on Render. Additionally, provide cloud-native deployment configurations (`Dockerfile` and `render.yaml`) to deploy the `demo-website` Java application directly to Render.

---

## 2. Architecture & Components

### 2.1 MCP Client Configuration (`.mcp.json`)
Register the `render` MCP server alongside the existing `java-playwright-mcp` server:

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

### 2.2 Cloud-Native Environment Adaptations (`DemoWebServer.java`)
Render dynamic HTTP port binding via the `PORT` environment variable:
- Read `System.getenv("PORT")`.
- If missing, fall back to CLI argument `args[0]`.
- If missing/invalid, fall back to default port `8080`.

### 2.3 Docker Packaging (`Dockerfile`)
Multi-stage build strategy:
1. **Build Stage:** `maven:3.9.6-eclipse-temurin-21-alpine`
   - Copy `pom.xml` files and source code.
   - Run `mvn clean package -DskipTests`.
2. **Runtime Stage:** `eclipse-temurin:21-jre-alpine`
   - Copy built `demo-website-1.0.0-SNAPSHOT.jar` from build stage.
   - Expose port `8080`.
   - Set ENTRYPOINT to `["java", "-jar", "demo-website.jar"]`.

### 2.4 Render Blueprint (`render.yaml`)
Declarative Infrastructure-as-Code specification:
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

---

## 3. Documentation Updates

1. **`docs/deployment.md`**:
   - Detailed guide for configuring `RENDER_API_KEY`.
   - Instructions for using Render MCP server tools via MCP clients.
   - Step-by-step instructions for deploying via Render Blueprint (`render.yaml`) or Render Dashboard.
2. **`README.md`**:
   - Reference Render MCP server and cloud deployment instructions in the execution and setup section.

---

## 4. Verification Plan

1. **Local Build & Execution**:
   - Verify `mvn clean package` succeeds.
   - Verify `DemoWebServer` respects `PORT` environment variable (e.g. `PORT=9090 java -jar ...`).
2. **Docker Container Verification**:
   - Build image: `docker build -t jira-mcp-demo-website .`
   - Run container: `docker run -p 8080:8080 -e PORT=8080 jira-mcp-demo-website`
   - Check http://localhost:8080 responds with HTML index.
3. **MCP Configuration Verification**:
   - Validate `.mcp.json` syntax.
