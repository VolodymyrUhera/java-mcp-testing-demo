package com.retro90s.mcp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeServiceTest {

    private KnowledgeService knowledgeService;

    @BeforeEach
    void setUp() {
        knowledgeService = new KnowledgeService();
    }

    @Test
    @DisplayName("Should initialize and load all knowledge items across 15 categories")
    void testInitializationAndIndexing() {
        List<KnowledgeItem> allItems = knowledgeService.getAllItems();
        assertNotNull(allItems, "All items list should not be null");
        assertFalse(allItems.isEmpty(), "Should load items from resources");
        assertTrue(allItems.size() >= 75, "Expected at least 75 items loaded from 15 JSON datasets");

        Optional<KnowledgeItem> itemOpt = knowledgeService.findById("consoles-snes-1990");
        assertTrue(itemOpt.isPresent(), "Should find item by exact ID");
        KnowledgeItem item = itemOpt.get();
        assertEquals("consoles-snes-1990", item.id());
        assertEquals("Super Nintendo Entertainment System (SNES)", item.title());
        assertEquals("consoles", item.category());
        assertEquals(1990, item.year());
        assertEquals("Nintendo", item.manufacturer());
    }

    @Test
    @DisplayName("Should lookup items by category case-insensitively")
    void testFindByCategory() {
        List<KnowledgeItem> consoles = knowledgeService.findByCategory("consoles");
        assertFalse(consoles.isEmpty(), "Category 'consoles' should not be empty");
        assertTrue(consoles.stream().allMatch(i -> "consoles".equalsIgnoreCase(i.category())));

        List<KnowledgeItem> uppercaseCategory = knowledgeService.findByCategory("CONSOLES");
        assertEquals(consoles.size(), uppercaseCategory.size(), "Lookup should be case-insensitive");

        List<KnowledgeItem> nonExistent = knowledgeService.findByCategory("nonexistent");
        assertTrue(nonExistent.isEmpty(), "Non-existent category should return empty list");
    }

    @Test
    @DisplayName("Should filter items by release year")
    void testFindByYear() {
        List<KnowledgeItem> items1995 = knowledgeService.findByYear(1995);
        assertFalse(items1995.isEmpty(), "Items for year 1995 should be found");
        assertTrue(items1995.stream().allMatch(i -> i.year() == 1995));

        List<KnowledgeItem> items1800 = knowledgeService.findByYear(1800);
        assertTrue(items1800.isEmpty(), "No items should exist for year 1800");
    }

    @Test
    @DisplayName("Should retrieve a random knowledge item")
    void testGetRandomItem() {
        Optional<KnowledgeItem> randomOpt = knowledgeService.getRandomItem();
        assertTrue(randomOpt.isPresent(), "Random item should be present");
        assertNotNull(randomOpt.get().id());
    }

    @Test
    @DisplayName("Should search using exact match, keyword score matching, fuzzy match, and year query")
    void testSearchAlgorithm() {
        // 1. Exact Match on ID
        List<KnowledgeItem> exactIdResults = knowledgeService.search("consoles-snes-1990");
        assertFalse(exactIdResults.isEmpty());
        assertEquals("consoles-snes-1990", exactIdResults.getFirst().id());

        // 2. Keyword score match
        List<KnowledgeItem> nintendoResults = knowledgeService.search("Nintendo");
        assertFalse(nintendoResults.isEmpty());
        assertTrue(nintendoResults.stream().anyMatch(i -> i.title().contains("Nintendo") || "Nintendo".equalsIgnoreCase(i.manufacturer())));

        // 3. Fuzzy match (typo handling: "playstasion" -> "PlayStation")
        List<KnowledgeItem> fuzzyResults = knowledgeService.search("playstasion");
        assertFalse(fuzzyResults.isEmpty(), "Fuzzy match should find results for typos");
        assertTrue(fuzzyResults.stream().anyMatch(i -> i.id().contains("ps1") || i.title().toLowerCase().contains("playstation")));

        // 4. Search by year query
        List<KnowledgeItem> yearResults = knowledgeService.search("1998");
        assertFalse(yearResults.isEmpty());

        // 5. Blank query
        assertTrue(knowledgeService.search("  ").isEmpty());
    }

    @Test
    @DisplayName("Should provide resource helper methods for timeline and category presets")
    void testResourceMethods() {
        // Timeline
        List<KnowledgeItem> timeline = knowledgeService.getTimeline();
        assertFalse(timeline.isEmpty());
        for (int i = 0; i < timeline.size() - 1; i++) {
            assertTrue(timeline.get(i).year() <= timeline.get(i + 1).year(), "Timeline should be sorted by year ascending");
        }

        // Operating Systems
        List<KnowledgeItem> osList = knowledgeService.getOperatingSystems();
        assertFalse(osList.isEmpty());
        assertTrue(osList.stream().allMatch(i ->
                "windows".equalsIgnoreCase(i.category()) ||
                "linux".equalsIgnoreCase(i.category()) ||
                "dos".equalsIgnoreCase(i.category())));

        // Consoles, Programming, Internet
        assertFalse(knowledgeService.getConsoles().isEmpty());
        assertFalse(knowledgeService.getProgramming().isEmpty());
        assertFalse(knowledgeService.getInternet().isEmpty());

        // Personality prompt
        String prompt = knowledgeService.getPersonalityPrompt();
        assertNotNull(prompt);
        assertTrue(prompt.contains("Cyber-Steve"));
    }
}
