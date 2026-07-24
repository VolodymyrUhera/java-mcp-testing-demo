package com.retro90s.mcp;

/**
 * Application entrypoint initializing services and starting Retro90sServer.
 */
public class Main {

    public static void main(String[] args) {
        int port = 8080;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            try {
                port = Integer.parseInt(envPort);
            } catch (NumberFormatException e) {
                System.err.println("Invalid PORT environment variable, defaulting to 8080: " + envPort);
            }
        }

        try {
            System.out.println("Initializing Retro90s Knowledge Base & Services...");
            KnowledgeService knowledgeService = new KnowledgeService();
            SearchService searchService = new SearchService();
            ToolRegistry toolRegistry = new ToolRegistry(knowledgeService, searchService);

            Retro90sServer server = new Retro90sServer(port, knowledgeService, searchService, toolRegistry);
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown signal received. Stopping server...");
                server.stop();
            }));

            System.out.println("Retro90s MCP Server is ready to serve requests! 🕹️⚡");
        } catch (Exception e) {
            System.err.println("Failed to start Retro90s MCP Server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
