package com.retro90s.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SearchService performs online fallback searches via Wikipedia REST API and DuckDuckGo API
 * using Java 21 HttpClient and Jackson. Synthesizes responses into KnowledgeItem objects.
 */
public class SearchService {

    private static final String DEFAULT_WIKI_URL_TEMPLATE = "https://en.wikipedia.org/api/rest_v1/page/summary/%s";
    private static final String DEFAULT_DDG_URL_TEMPLATE = "https://api.duckduckgo.com/?q=%s&format=json";
    private static final Pattern NINETIES_YEAR_PATTERN = Pattern.compile("\\b(199\\d)\\b");

    private final HttpClient httpClient;
    private final String wikiUrlTemplate;
    private final String duckDuckGoUrlTemplate;
    private final ObjectMapper objectMapper;

    public SearchService() {
        this(
            HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build(),
            DEFAULT_WIKI_URL_TEMPLATE,
            DEFAULT_DDG_URL_TEMPLATE
        );
    }

    public SearchService(HttpClient httpClient) {
        this(httpClient, DEFAULT_WIKI_URL_TEMPLATE, DEFAULT_DDG_URL_TEMPLATE);
    }

    public SearchService(HttpClient httpClient, String wikiUrlTemplate, String duckDuckGoUrlTemplate) {
        this.httpClient = Objects.requireNonNull(httpClient, "HttpClient must not be null");
        this.wikiUrlTemplate = Objects.requireNonNull(wikiUrlTemplate, "wikiUrlTemplate must not be null");
        this.duckDuckGoUrlTemplate = Objects.requireNonNull(duckDuckGoUrlTemplate, "duckDuckGoUrlTemplate must not be null");
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Performs a search against Wikipedia REST API.
     *
     * @param query Search query term
     * @return Optional containing synthesized KnowledgeItem if successful
     */
    public Optional<KnowledgeItem> searchWikipedia(String query) {
        if (query == null || query.isBlank()) {
            return Optional.empty();
        }

        try {
            String sanitizedQuery = query.trim().replace(" ", "_");
            String encodedQuery = URLEncoder.encode(sanitizedQuery, StandardCharsets.UTF_8);
            String targetUrl = String.format(wikiUrlTemplate, encodedQuery);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .header("User-Agent", "Retro90s-MCP/1.0 (Retro Knowledge Bot)")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body() == null || response.body().isBlank()) {
                return Optional.empty();
            }

            JsonNode node = objectMapper.readTree(response.body());
            String type = node.path("type").asText("");
            if ("not_found".equalsIgnoreCase(type)) {
                return Optional.empty();
            }

            String title = node.path("title").asText(query.trim());
            String extract = node.path("extract").asText(node.path("description").asText(""));
            if (extract.isBlank()) {
                return Optional.empty();
            }

            String id = "wiki-" + sanitizeSlug(title);
            int year = extractNinetiesYear(extract, 1990);
            List<String> facts = extractFacts(extract);
            List<String> keywords = buildKeywords(query, title, "wikipedia");

            KnowledgeItem item = new KnowledgeItem(
                id,
                title,
                "online-search-wikipedia",
                year,
                "Wikipedia",
                extract,
                facts,
                Collections.emptyList(),
                keywords
            );

            return Optional.of(item);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Performs a search against DuckDuckGo Instant Answer API.
     *
     * @param query Search query term
     * @return Optional containing synthesized KnowledgeItem if successful
     */
    public Optional<KnowledgeItem> searchDuckDuckGo(String query) {
        if (query == null || query.isBlank()) {
            return Optional.empty();
        }

        try {
            String encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
            String targetUrl = String.format(duckDuckGoUrlTemplate, encodedQuery);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targetUrl))
                .header("User-Agent", "Retro90s-MCP/1.0 (Retro Knowledge Bot)")
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body() == null || response.body().isBlank()) {
                return Optional.empty();
            }

            JsonNode node = objectMapper.readTree(response.body());
            String heading = node.path("Heading").asText(query.trim());
            String abstractText = node.path("AbstractText").asText(node.path("Abstract").asText(""));
            String abstractSource = node.path("AbstractSource").asText("DuckDuckGo");

            if (abstractText.isBlank()) {
                return Optional.empty();
            }

            String id = "ddg-" + sanitizeSlug(heading);
            int year = extractNinetiesYear(abstractText, 1990);
            List<String> facts = extractFacts(abstractText);
            List<String> related = extractRelatedTopics(node.path("RelatedTopics"));
            List<String> keywords = buildKeywords(query, heading, abstractSource);

            KnowledgeItem item = new KnowledgeItem(
                id,
                heading,
                "online-search-duckduckgo",
                year,
                abstractSource.isBlank() ? "DuckDuckGo" : abstractSource,
                abstractText,
                facts,
                related,
                keywords
            );

            return Optional.of(item);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Performs an online search trying Wikipedia first, then DuckDuckGo.
     *
     * @param query Search query term
     * @return Optional containing synthesized KnowledgeItem if found
     */
    public Optional<KnowledgeItem> searchOnline(String query) {
        Optional<KnowledgeItem> wikiResult = searchWikipedia(query);
        if (wikiResult.isPresent()) {
            return wikiResult;
        }
        return searchDuckDuckGo(query);
    }

    /**
     * Performs an online search with fallback logic.
     * Tries Wikipedia first, then DuckDuckGo, and returns a safe fallback KnowledgeItem if both fail or errors occur.
     *
     * @param query Search query term
     * @return KnowledgeItem synthesized from online search or safe fallback object
     */
    public KnowledgeItem searchWithFallback(String query) {
        if (query == null || query.isBlank()) {
            return createFallbackItem("Unknown Query");
        }

        Optional<KnowledgeItem> onlineResult = searchOnline(query);
        return onlineResult.orElseGet(() -> createFallbackItem(query.trim()));
    }

    private KnowledgeItem createFallbackItem(String query) {
        String safeQuery = (query == null || query.isBlank()) ? "Unknown Query" : query;
        return new KnowledgeItem(
            "fallback-" + sanitizeSlug(safeQuery),
            safeQuery,
            "fallback",
            1990,
            "Retro90s Fallback Engine",
            "No online results found for query: " + safeQuery + ". Returning default 1990s fallback item.",
            List.of("Online search services were unreachable or returned no results."),
            Collections.emptyList(),
            List.of(safeQuery.toLowerCase(), "fallback", "retro90s")
        );
    }

    private String sanitizeSlug(String text) {
        if (text == null || text.isBlank()) {
            return "item";
        }
        return text.trim().toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-+|-+$", "");
    }

    private int extractNinetiesYear(String text, int defaultYear) {
        if (text == null) {
            return defaultYear;
        }
        Matcher matcher = NINETIES_YEAR_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ignored) {
            }
        }
        return defaultYear;
    }

    private List<String> extractFacts(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        String[] sentences = text.split("\\.\\s+");
        List<String> facts = new ArrayList<>();
        for (String sentence : sentences) {
            String trimmed = sentence.trim();
            if (!trimmed.endsWith(".")) {
                trimmed += ".";
            }
            if (trimmed.length() > 5) {
                facts.add(trimmed);
            }
        }
        return Collections.unmodifiableList(facts);
    }

    private List<String> extractRelatedTopics(JsonNode relatedNode) {
        if (relatedNode == null || !relatedNode.isArray()) {
            return Collections.emptyList();
        }
        List<String> related = new ArrayList<>();
        for (JsonNode child : relatedNode) {
            if (child.has("Text")) {
                String topicText = child.path("Text").asText();
                if (!topicText.isBlank()) {
                    related.add(topicText);
                }
            } else if (child.has("Topics") && child.path("Topics").isArray()) {
                for (JsonNode subChild : child.path("Topics")) {
                    String topicText = subChild.path("Text").asText();
                    if (!topicText.isBlank()) {
                        related.add(topicText);
                    }
                }
            }
        }
        return Collections.unmodifiableList(related);
    }

    private List<String> buildKeywords(String query, String title, String source) {
        Set<String> keywords = new LinkedHashSet<>();
        if (query != null && !query.isBlank()) {
            keywords.add(query.trim().toLowerCase());
        }
        if (title != null && !title.isBlank()) {
            keywords.add(title.trim().toLowerCase());
        }
        if (source != null && !source.isBlank()) {
            keywords.add(source.trim().toLowerCase());
        }
        keywords.add("retro90s");
        keywords.add("online-search");
        return List.copyOf(keywords);
    }
}
