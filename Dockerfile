# Stage 1: Build application using Maven & Temurin JDK 21
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY demo-website demo-website
COPY mcp-server mcp-server
COPY retro90s-mcp retro90s-mcp
COPY testing-scenarios testing-scenarios
RUN mvn clean package -DskipTests

# Stage 2: Minimal JRE 21 Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/demo-website/target/demo-website-1.0.0-SNAPSHOT-jar-with-dependencies.jar /app/demo-website.jar
COPY --from=builder /app/retro90s-mcp/target/retro90s-mcp-1.0.0-SNAPSHOT-jar-with-dependencies.jar /app/retro90s-mcp.jar
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

ENV PORT=8080
ENV RETRO90S_MCP_URL=http://localhost:8081/message
EXPOSE 8080 8081

ENTRYPOINT ["/app/start.sh"]
