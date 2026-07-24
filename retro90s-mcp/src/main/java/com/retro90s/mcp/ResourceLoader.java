package com.retro90s.mcp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceLoader loads knowledge base JSON datasets and prompt resource files from the classpath.
 */
public class ResourceLoader {

    public static final List<String> KNOWLEDGE_FILES = List.of(
        "brands.json",
        "consoles.json",
        "dos.json",
        "fashion.json",
        "games.json",
        "hardware.json",
        "history.json",
        "internet.json",
        "linux.json",
        "movies.json",
        "music.json",
        "programming.json",
        "technology.json",
        "television.json",
        "windows.json"
    );

    public static final String PERSONALITY_PROMPT_PATH = "/prompts/personality.md";

    private final ObjectMapper objectMapper;

    public ResourceLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public ResourceLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Loads a resource file as a UTF-8 String from classpath.
     *
     * @param resourcePath Path to the resource (e.g. "/prompts/personality.md" or "/knowledge/consoles.json")
     * @return String content of the resource
     */
    public String loadResourceAsString(String resourcePath) {
        String normalizedPath = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
        try (InputStream inputStream = ResourceLoader.class.getResourceAsStream(normalizedPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found on classpath: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }

    /**
     * Loads all 15 JSON datasets from classpath into a single list of KnowledgeItems.
     *
     * @return List of all loaded KnowledgeItems
     */
    public List<KnowledgeItem> loadAllKnowledgeItems() {
        List<KnowledgeItem> allItems = new ArrayList<>();
        for (String fileName : KNOWLEDGE_FILES) {
            String path = "/knowledge/" + fileName;
            List<KnowledgeItem> items = loadKnowledgeFile(path);
            allItems.addAll(items);
        }
        return allItems;
    }

    /**
     * Loads a single JSON dataset from classpath into a list of KnowledgeItems.
     *
     * @param resourcePath Path to JSON resource
     * @return List of KnowledgeItems parsed from JSON
     */
    public List<KnowledgeItem> loadKnowledgeFile(String resourcePath) {
        String jsonContent = loadResourceAsString(resourcePath);
        try {
            return objectMapper.readValue(jsonContent, new TypeReference<List<KnowledgeItem>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON from resource: " + resourcePath, e);
        }
    }

    /**
     * Loads the Cyber-Steve personality prompt from "/prompts/personality.md".
     *
     * @return String content of the personality prompt
     */
    public String loadPersonalityPrompt() {
        return loadResourceAsString(PERSONALITY_PROMPT_PATH);
    }
}
