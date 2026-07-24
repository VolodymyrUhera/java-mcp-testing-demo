package com.retro90s.mcp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * KnowledgeService manages retro 90s knowledge items in-memory.
 * Provides searching (exact, keyword score, fuzzy), indexing by category/year/id,
 * random item selection, and pre-filtered views for resources.
 */
public class KnowledgeService {

    private final ResourceLoader resourceLoader;
    private final List<KnowledgeItem> allItems;
    private final Map<String, KnowledgeItem> itemsById;
    private final Map<String, List<KnowledgeItem>> itemsByCategory;
    private final Map<Integer, List<KnowledgeItem>> itemsByYear;
    private final Random random;

    public KnowledgeService() {
        this(new ResourceLoader());
    }

    public KnowledgeService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.random = new Random();
        this.allItems = new ArrayList<>();
        this.itemsById = new HashMap<>();
        this.itemsByCategory = new HashMap<>();
        this.itemsByYear = new HashMap<>();
        init();
    }

    private void init() {
        List<KnowledgeItem> loaded = resourceLoader.loadAllKnowledgeItems();
        for (KnowledgeItem item : loaded) {
            allItems.add(item);
            if (item.id() != null) {
                itemsById.put(item.id().toLowerCase(), item);
            }
            if (item.category() != null) {
                itemsByCategory.computeIfAbsent(item.category().toLowerCase(), k -> new ArrayList<>()).add(item);
            }
            itemsByYear.computeIfAbsent(item.year(), k -> new ArrayList<>()).add(item);
        }
    }

    public List<KnowledgeItem> getAllItems() {
        return Collections.unmodifiableList(allItems);
    }

    public Optional<KnowledgeItem> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(itemsById.get(id.trim().toLowerCase()));
    }

    public List<KnowledgeItem> findByCategory(String category) {
        if (category == null) {
            return Collections.emptyList();
        }
        List<KnowledgeItem> list = itemsByCategory.get(category.trim().toLowerCase());
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }

    public List<KnowledgeItem> findByYear(int year) {
        List<KnowledgeItem> list = itemsByYear.get(year);
        return list != null ? Collections.unmodifiableList(list) : Collections.emptyList();
    }

    public Optional<KnowledgeItem> getRandomItem() {
        if (allItems.isEmpty()) {
            return Optional.empty();
        }
        int index = random.nextInt(allItems.size());
        return Optional.of(allItems.get(index));
    }

    /**
     * Search knowledge base using exact match, keyword score matching, and fuzzy matching.
     *
     * @param query Search query string
     * @return List of matching KnowledgeItems sorted by relevance score descending
     */
    public List<KnowledgeItem> search(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        String normalizedQuery = query.trim().toLowerCase();
        String[] tokens = normalizedQuery.split("\\s+");

        Map<KnowledgeItem, Integer> scores = new HashMap<>();

        for (KnowledgeItem item : allItems) {
            int score = calculateScore(item, normalizedQuery, tokens);
            if (score > 0) {
                scores.put(item, score);
            }
        }

        return scores.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int calculateScore(KnowledgeItem item, String fullQuery, String[] tokens) {
        int score = 0;

        // 1. Exact Match bonuses
        if (item.id() != null && item.id().equalsIgnoreCase(fullQuery)) {
            score += 100;
        }
        if (item.title() != null && item.title().equalsIgnoreCase(fullQuery)) {
            score += 80;
        }
        if (item.category() != null && item.category().equalsIgnoreCase(fullQuery)) {
            score += 40;
        }
        if (item.keywords() != null) {
            for (String kw : item.keywords()) {
                if (kw.equalsIgnoreCase(fullQuery)) {
                    score += 60;
                    break;
                }
            }
        }

        // Substring match on full query
        if (item.title() != null && item.title().toLowerCase().contains(fullQuery)) {
            score += 30;
        }
        if (item.summary() != null && item.summary().toLowerCase().contains(fullQuery)) {
            score += 15;
        }
        if (String.valueOf(item.year()).equals(fullQuery)) {
            score += 30;
        }

        // 2. Keyword score matching for each token
        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (item.id() != null && item.id().toLowerCase().contains(token)) {
                score += 15;
            }
            if (item.title() != null && item.title().toLowerCase().contains(token)) {
                score += 20;
            }
            if (item.category() != null && item.category().toLowerCase().contains(token)) {
                score += 10;
            }
            if (item.manufacturer() != null && item.manufacturer().toLowerCase().contains(token)) {
                score += 10;
            }
            if (item.keywords() != null) {
                for (String kw : item.keywords()) {
                    if (kw.toLowerCase().contains(token)) {
                        score += 15;
                        break;
                    }
                }
            }
            if (item.summary() != null && item.summary().toLowerCase().contains(token)) {
                score += 5;
            }
            if (item.facts() != null) {
                for (String fact : item.facts()) {
                    if (fact.toLowerCase().contains(token)) {
                        score += 3;
                        break;
                    }
                }
            }
        }

        // 3. Fuzzy matching for query/tokens against title, keywords, category, manufacturer
        if (score == 0) {
            for (String token : tokens) {
                if (token.length() >= 4) {
                    if (item.title() != null && isFuzzyMatch(token, item.title())) {
                        score += 15;
                    }
                    if (item.category() != null && isFuzzyMatch(token, item.category())) {
                        score += 10;
                    }
                    if (item.keywords() != null) {
                        for (String kw : item.keywords()) {
                            if (isFuzzyMatch(token, kw)) {
                                score += 12;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return score;
    }

    private boolean isFuzzyMatch(String token, String targetText) {
        String[] targetWords = targetText.toLowerCase().split("[\\s\\-_()/,.]+");
        for (String word : targetWords) {
            if (word.length() >= 3) {
                int dist = levenshteinDistance(token, word);
                if (dist <= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    private int levenshteinDistance(String a, String b) {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    // Resources Methods

    /**
     * Gets all timeline items sorted chronologically by year (ascending).
     */
    public List<KnowledgeItem> getTimeline() {
        return allItems.stream()
                .sorted(Comparator.comparingInt(KnowledgeItem::year)
                        .thenComparing(KnowledgeItem::id))
                .collect(Collectors.toList());
    }

    /**
     * Gets operating systems knowledge items (windows, linux, dos categories).
     */
    public List<KnowledgeItem> getOperatingSystems() {
        return allItems.stream()
                .filter(item -> item.category() != null &&
                        (item.category().equalsIgnoreCase("windows") ||
                         item.category().equalsIgnoreCase("linux") ||
                         item.category().equalsIgnoreCase("dos")))
                .sorted(Comparator.comparingInt(KnowledgeItem::year))
                .collect(Collectors.toList());
    }

    /**
     * Gets consoles knowledge items.
     */
    public List<KnowledgeItem> getConsoles() {
        return findByCategory("consoles");
    }

    /**
     * Gets programming knowledge items.
     */
    public List<KnowledgeItem> getProgramming() {
        return findByCategory("programming");
    }

    /**
     * Gets internet knowledge items.
     */
    public List<KnowledgeItem> getInternet() {
        return findByCategory("internet");
    }

    /**
     * Gets the Cyber-Steve personality prompt string.
     */
    public String getPersonalityPrompt() {
        return resourceLoader.loadPersonalityPrompt();
    }
}
