package com.demo.website;

import com.demo.website.handlers.AboutHandler;
import com.demo.website.handlers.ContactHandler;
import com.demo.website.handlers.HomeHandler;
import com.demo.website.handlers.LinksHandler;
import com.demo.website.handlers.StaticAssetHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class DemoWebServer {
    private static final Logger LOGGER = Logger.getLogger(DemoWebServer.class.getName());
    private static final int DEFAULT_PORT = 8080;
    
    private final HttpServer server;
    private final int port;

    public DemoWebServer(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Register HTTP context handlers
        server.createContext("/", new HomeHandler());
        server.createContext("/about", new AboutHandler());
        server.createContext("/contact", new ContactHandler());
        server.createContext("/links", new LinksHandler());
        server.createContext("/static", new StaticAssetHandler());
        
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    public void start() {
        server.start();
        LOGGER.info("Demo Web Server (CyberSpace 1999) started on http://localhost:" + port);
    }

    public void stop() {
        server.stop(0);
        LOGGER.info("Demo Web Server stopped.");
    }

    public int getPort() {
        return port;
    }

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }

        DemoWebServer webServer = new DemoWebServer(port);
        webServer.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(webServer::stop));
    }
}
