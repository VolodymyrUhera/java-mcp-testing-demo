#!/bin/sh
# Start Retro 90s MCP Server in background on port 8081
echo "Starting Retro 90s MCP Server on port 8081..."
PORT=8081 java -jar /app/retro90s-mcp.jar &

# Allow retro90s-mcp server to initialize
sleep 2

# Start Demo Website HTTP Server on PORT (default 8080)
echo "Starting Demo Web Server on port ${PORT:-8080}..."
exec java -jar /app/demo-website.jar
