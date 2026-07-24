package com.demo.website.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticAssetHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String resourcePath = "/static" + path.substring("/static".length());
        
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                String notFound = "404 Not Found";
                exchange.sendResponseHeaders(404, notFound.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound.getBytes());
                }
                return;
            }
            
            byte[] bytes = is.readAllBytes();
            if (path.endsWith(".css")) {
                exchange.getResponseHeaders().set("Content-Type", "text/css");
            } else if (path.endsWith(".svg")) {
                exchange.getResponseHeaders().set("Content-Type", "image/svg+xml");
            } else if (path.endsWith(".gif")) {
                exchange.getResponseHeaders().set("Content-Type", "image/gif");
            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                exchange.getResponseHeaders().set("Content-Type", "image/jpeg");
            } else if (path.endsWith(".png")) {
                exchange.getResponseHeaders().set("Content-Type", "image/png");
            } else {
                exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
            }
            
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
