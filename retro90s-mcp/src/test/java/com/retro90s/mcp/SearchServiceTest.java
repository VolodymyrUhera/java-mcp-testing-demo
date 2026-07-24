package com.retro90s.mcp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SearchServiceTest {

    private HttpServer mockServer;
    private int port;
    private SearchService searchService;
    private DynamicHandler wikiHandler;
    private DynamicHandler ddgHandler;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = HttpServer.create(new InetSocketAddress(0), 0);
        port = mockServer.getAddress().getPort();

        wikiHandler = new DynamicHandler();
        ddgHandler = new DynamicHandler();

        mockServer.createContext("/wiki", wikiHandler);
        mockServer.createContext("/ddg", ddgHandler);
        mockServer.start();

        String wikiTemplate = "http://localhost:" + port + "/wiki/%s";
        String ddgTemplate = "http://localhost:" + port + "/ddg/%s";

        searchService = new SearchService(HttpClient.newHttpClient(), wikiTemplate, ddgTemplate);
    }

    @AfterEach
    void tearDown() {
        if (mockServer != null) {
            mockServer.stop(0);
        }
    }

    @Test
    void testSearchWikipediaSuccess() {
        String wikiJson = """
            {
              "title": "PlayStation",
              "extract": "The PlayStation is a video game brand created and produced by Sony Computer Entertainment in 1994.",
              "description": "Video game brand",
              "type": "standard"
            }
            """;
        wikiHandler.setDefaultResponse(200, wikiJson);

        Optional<KnowledgeItem> result = searchService.searchWikipedia("PlayStation");

        assertTrue(result.isPresent());
        KnowledgeItem item = result.get();
        assertEquals("PlayStation", item.title());
        assertEquals("Wikipedia", item.manufacturer());
        assertEquals(1994, item.year());
        assertTrue(item.summary().contains("Sony Computer Entertainment"));
        assertTrue(item.keywords().contains("playstation"));
    }

    @Test
    void testSearchDuckDuckGoSuccess() {
        String ddgJson = """
            {
              "Heading": "Windows 95",
              "Abstract": "Windows 95 is a consumer-oriented operating system developed by Microsoft released in 1995.",
              "AbstractSource": "Wikipedia",
              "RelatedTopics": [
                {"Text": "Windows 98"},
                {"Text": "MS-DOS"}
              ]
            }
            """;
        ddgHandler.setDefaultResponse(200, ddgJson);

        Optional<KnowledgeItem> result = searchService.searchDuckDuckGo("Windows 95");

        assertTrue(result.isPresent());
        KnowledgeItem item = result.get();
        assertEquals("Windows 95", item.title());
        assertEquals("Wikipedia", item.manufacturer());
        assertEquals(1995, item.year());
        assertTrue(item.summary().contains("consumer-oriented operating system"));
        assertTrue(item.related().contains("Windows 98"));
    }

    @Test
    void testSearchWithFallbackWikipediaFirst() {
        String wikiJson = """
            {
              "title": "Game Boy",
              "extract": "Game Boy is an 8-bit handheld game console developed and manufactured by Nintendo.",
              "type": "standard"
            }
            """;
        wikiHandler.setDefaultResponse(200, wikiJson);

        KnowledgeItem item = searchService.searchWithFallback("Game Boy");

        assertNotNull(item);
        assertEquals("Game Boy", item.title());
        assertEquals("Wikipedia", item.manufacturer());
    }

    @Test
    void testSearchWithFallbackDuckDuckGoSecondary() {
        wikiHandler.setDefaultResponse(404, "Not Found");

        String ddgJson = """
            {
              "Heading": "Netscape Navigator",
              "Abstract": "Netscape Navigator was a web browser produced by Netscape Communications Corporation in 1994.",
              "AbstractSource": "DuckDuckGo"
            }
            """;
        ddgHandler.setDefaultResponse(200, ddgJson);

        KnowledgeItem item = searchService.searchWithFallback("Netscape");

        assertNotNull(item);
        assertEquals("Netscape Navigator", item.title());
        assertTrue(item.summary().contains("web browser"));
    }

    @Test
    void testSearchWithFallbackGracefulFailure() {
        wikiHandler.setDefaultResponse(500, "Error");
        ddgHandler.setDefaultResponse(500, "Error");

        KnowledgeItem item = searchService.searchWithFallback("UnknownTopic");

        assertNotNull(item);
        assertEquals("UnknownTopic", item.title());
        assertEquals("fallback", item.category());
        assertTrue(item.summary().contains("No online results found"));
    }

    @Test
    void testNetworkErrorGracefullyHandled() {
        SearchService brokenService = new SearchService(
            HttpClient.newHttpClient(),
            "http://localhost:1/wiki/%s",
            "http://localhost:1/ddg/%s"
        );

        Optional<KnowledgeItem> wikiResult = brokenService.searchWikipedia("Test");
        Optional<KnowledgeItem> ddgResult = brokenService.searchDuckDuckGo("Test");
        KnowledgeItem fallbackResult = brokenService.searchWithFallback("Test");

        assertTrue(wikiResult.isEmpty());
        assertTrue(ddgResult.isEmpty());
        assertNotNull(fallbackResult);
        assertEquals("fallback", fallbackResult.category());
    }

    @Test
    void testNullAndBlankQueries() {
        assertTrue(searchService.searchWikipedia(null).isEmpty());
        assertTrue(searchService.searchWikipedia("   ").isEmpty());
        assertTrue(searchService.searchDuckDuckGo(null).isEmpty());
        assertTrue(searchService.searchDuckDuckGo("").isEmpty());

        KnowledgeItem fallbackNull = searchService.searchWithFallback(null);
        assertNotNull(fallbackNull);
        assertEquals("fallback", fallbackNull.category());
    }

    private static class DynamicHandler implements HttpHandler {
        private int defaultStatusCode = 404;
        private String defaultBody = "Not Found";

        void setDefaultResponse(int statusCode, String body) {
            this.defaultStatusCode = statusCode;
            this.defaultBody = body;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] bytes = defaultBody.getBytes();
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(defaultStatusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
