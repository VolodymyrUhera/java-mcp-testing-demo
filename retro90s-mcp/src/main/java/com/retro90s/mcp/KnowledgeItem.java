package com.retro90s.mcp;

import java.util.List;

/**
 * Record representing a single 1990s knowledge item.
 */
public record KnowledgeItem(
    String id,
    String title,
    String category,
    int year,
    String manufacturer,
    String summary,
    List<String> facts,
    List<String> related,
    List<String> keywords
) {}
