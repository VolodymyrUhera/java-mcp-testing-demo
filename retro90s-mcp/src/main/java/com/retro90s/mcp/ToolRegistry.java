package com.retro90s.mcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * ToolRegistry configures schemas for tools/list and handles tool execution for all 9 MCP tools:
 * ask90s, compare, recommend, explain, trivia, nostalgia, year, website, hardware.
 */
public class ToolRegistry {

    private final KnowledgeService knowledgeService;
    private final SearchService searchService;
    private final ObjectMapper objectMapper;

    public ToolRegistry() {
        this(new KnowledgeService(), new SearchService());
    }

    public ToolRegistry(KnowledgeService knowledgeService, SearchService searchService) {
        this.knowledgeService = Objects.requireNonNull(knowledgeService, "KnowledgeService must not be null");
        this.searchService = Objects.requireNonNull(searchService, "SearchService must not be null");
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Returns the array of 9 MCP tool definitions for tools/list.
     */
    public ArrayNode listTools() {
        ArrayNode tools = objectMapper.createArrayNode();

        tools.add(createToolDefinition(
            "ask90s",
            "Ask Cyber-Steve any question about 90s technology, software, hardware, pop culture, or historical events.",
            createObjectSchema(
                Map.of("question", createStringProperty("The 90s question or topic to search and ask about.")),
                List.of("question")
            )
        ));

        tools.add(createToolDefinition(
            "compare",
            "Compare two 90s items, software, hardware, or pop culture phenomena.",
            createObjectSchema(
                Map.of(
                    "left", createStringProperty("First 90s item or concept to compare."),
                    "right", createStringProperty("Second 90s item or concept to compare.")
                ),
                List.of("left", "right")
            )
        ));

        tools.add(createToolDefinition(
            "recommend",
            "Get top 90s recommendations for a given category or random picks.",
            createObjectSchema(
                Map.of("category", createStringProperty("Knowledge domain category (e.g. games, movies, consoles, music, hardware, technology, internet).")),
                Collections.emptyList()
            )
        ));

        tools.add(createToolDefinition(
            "explain",
            "In-depth historical and technical explanation of a 90s concept, technology, or event.",
            createObjectSchema(
                Map.of("topic", createStringProperty("The 90s topic or technical concept to explain in detail.")),
                List.of("topic")
            )
        ));

        tools.add(createToolDefinition(
            "trivia",
            "Get random 90s trivia question or obscure historical facts.",
            createObjectSchema(
                Map.of("category", createStringProperty("Optional category to filter trivia (e.g. games, movies, technology).")),
                Collections.emptyList()
            )
        ));

        tools.add(createToolDefinition(
            "nostalgia",
            "Generate a nostalgic 90s memory trip, combining retro tech, culture, and Cyber-Steve commentary.",
            createObjectSchema(
                Map.of("theme", createStringProperty("Optional theme for the nostalgia trip (e.g. gaming, internet, music, weekend).")),
                Collections.emptyList()
            )
        ));

        tools.add(createToolDefinition(
            "year",
            "Get a comprehensive breakdown of major 90s releases and events for a specific year (1990-1999).",
            createObjectSchema(
                Map.of("year", createIntegerProperty("Year between 1990 and 1999.")),
                List.of("year")
            )
        ));

        tools.add(createToolDefinition(
            "website",
            "Explore 90s internet landmarks, early web browsers, search engines, and dot-com sites.",
            createObjectSchema(
                Map.of("name", createStringProperty("90s website, browser, or web portal name (e.g. GeoCities, AOL, AltaVista, Netscape).")),
                List.of("name")
            )
        ));

        tools.add(createToolDefinition(
            "hardware",
            "Get detailed technical specs and history for 90s hardware, CPUs, GPUs, and peripherals.",
            createObjectSchema(
                Map.of("component", createStringProperty("90s hardware component or device name (e.g. 3dfx Voodoo, Pentium, Zip Drive, Sound Blaster).")),
                List.of("component")
            )
        ));

        return tools;
    }

    /**
     * Executes the specified tool with arguments and returns an MCP tool result JsonNode.
     */
    public JsonNode callTool(String toolName, JsonNode arguments) {
        if (toolName == null || toolName.isBlank()) {
            return createToolResult("Tool name must not be null or blank", true);
        }

        return switch (toolName.trim().toLowerCase()) {
            case "ask90s" -> executeAsk90s(arguments);
            case "compare" -> executeCompare(arguments);
            case "recommend" -> executeRecommend(arguments);
            case "explain" -> executeExplain(arguments);
            case "trivia" -> executeTrivia(arguments);
            case "nostalgia" -> executeNostalgia(arguments);
            case "year" -> executeYear(arguments);
            case "website" -> executeWebsite(arguments);
            case "hardware" -> executeHardware(arguments);
            default -> createToolResult("Unknown tool: " + toolName, true);
        };
    }

    private JsonNode executeAsk90s(JsonNode args) {
        String question = getArgumentString(args, "question");
        if (question.isBlank()) {
            return createToolResult("Please provide a question about the 90s!", true);
        }

        List<KnowledgeItem> searchResults = knowledgeService.search(question);
        KnowledgeItem item;
        boolean isFallback = false;

        if (!searchResults.isEmpty()) {
            item = searchResults.get(0);
        } else {
            item = searchService.searchWithFallback(question);
            isFallback = true;
        }

        StringBuilder text = new StringBuilder();
        text.append("🕹️ Cyber-Steve says: Booyah! Here is the lowdown on '").append(question).append("':\n\n");
        text.append("📌 **").append(item.title()).append("** (").append(item.year()).append(")\n");
        if (item.manufacturer() != null && !item.manufacturer().isBlank()) {
            text.append("🏭 Manufacturer/Creator: ").append(item.manufacturer()).append("\n");
        }
        text.append("📁 Category: ").append(item.category()).append("\n\n");
        text.append("📝 ").append(item.summary()).append("\n");

        if (item.facts() != null && !item.facts().isEmpty()) {
            text.append("\n💡 **Radical Facts:**\n");
            for (String fact : item.facts()) {
                text.append(" - ").append(fact).append("\n");
            }
        }

        if (isFallback) {
            text.append("\n🌐 *(Retrieved via Cyber-Steve Online Search Engine)*");
        }

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("answer", text.toString());
        result.put("itemTitle", item.title());
        result.put("itemYear", item.year());
        return result;
    }

    private JsonNode executeCompare(JsonNode args) {
        String leftQuery = getArgumentString(args, "left");
        String rightQuery = getArgumentString(args, "right");

        if (leftQuery.isBlank() || rightQuery.isBlank()) {
            return createToolResult("Please provide both 'left' and 'right' items to compare!", true);
        }

        List<KnowledgeItem> leftResults = knowledgeService.search(leftQuery);
        KnowledgeItem leftItem = leftResults.isEmpty() ? searchService.searchWithFallback(leftQuery) : leftResults.get(0);

        List<KnowledgeItem> rightResults = knowledgeService.search(rightQuery);
        KnowledgeItem rightItem = rightResults.isEmpty() ? searchService.searchWithFallback(rightQuery) : rightResults.get(0);

        StringBuilder text = new StringBuilder();
        text.append("⚔️ **90s Showdown: ").append(leftItem.title()).append(" vs ").append(rightItem.title()).append("** ⚔️\n\n");

        text.append("1️⃣ **").append(leftItem.title()).append("**\n");
        text.append("   - Year: ").append(leftItem.year()).append("\n");
        text.append("   - Category: ").append(leftItem.category()).append("\n");
        text.append("   - Maker: ").append(leftItem.manufacturer() != null ? leftItem.manufacturer() : "N/A").append("\n");
        text.append("   - Summary: ").append(leftItem.summary()).append("\n\n");

        text.append("2️⃣ **").append(rightItem.title()).append("**\n");
        text.append("   - Year: ").append(rightItem.year()).append("\n");
        text.append("   - Category: ").append(rightItem.category()).append("\n");
        text.append("   - Maker: ").append(rightItem.manufacturer() != null ? rightItem.manufacturer() : "N/A").append("\n");
        text.append("   - Summary: ").append(rightItem.summary()).append("\n\n");

        text.append("💬 **Cyber-Steve Breakdown:** Both ").append(leftItem.title()).append(" (").append(leftItem.year())
            .append(") and ").append(rightItem.title()).append(" (").append(rightItem.year())
            .append(") defined the 1990s era in ").append(leftItem.category()).append("!");

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("comparison", text.toString());
        result.put("leftTitle", leftItem.title());
        result.put("rightTitle", rightItem.title());
        return result;
    }

    private JsonNode executeRecommend(JsonNode args) {
        String category = getArgumentString(args, "category");
        List<KnowledgeItem> items;

        if (!category.isBlank()) {
            items = knowledgeService.findByCategory(category);
            if (items.isEmpty()) {
                items = knowledgeService.search(category);
            }
        } else {
            items = knowledgeService.getAllItems();
        }

        if (items.isEmpty()) {
            KnowledgeItem fallback = searchService.searchWithFallback(category.isBlank() ? "90s recommendations" : category);
            items = List.of(fallback);
        }

        List<KnowledgeItem> recs = items.stream().limit(3).toList();

        StringBuilder text = new StringBuilder();
        String catHeader = category.isBlank() ? "Top 90s Picks" : category.toUpperCase();
        text.append("🌟 **Cyber-Steve's Fly Recommendations [").append(catHeader).append("]:**\n\n");

        for (int i = 0; i < recs.size(); i++) {
            KnowledgeItem item = recs.get(i);
            text.append(i + 1).append(". **").append(item.title()).append("** (").append(item.year()).append(")\n");
            text.append("   📁 Category: ").append(item.category()).append("\n");
            text.append("   📝 ").append(item.summary()).append("\n\n");
        }

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("recommendations", text.toString());
        result.put("category", category);
        return result;
    }

    private JsonNode executeExplain(JsonNode args) {
        String topic = getArgumentString(args, "topic");
        if (topic.isBlank()) {
            return createToolResult("Please provide a topic to explain!", true);
        }

        List<KnowledgeItem> searchResults = knowledgeService.search(topic);
        KnowledgeItem item = searchResults.isEmpty() ? searchService.searchWithFallback(topic) : searchResults.get(0);

        StringBuilder text = new StringBuilder();
        text.append("📖 **Radical Deep Dive: ").append(item.title()).append("** (").append(item.year()).append(")\n");
        text.append("═════════════════════════════════════════════\n");
        text.append("🏭 **Manufacturer / Creator:** ").append(item.manufacturer() != null ? item.manufacturer() : "N/A").append("\n");
        text.append("📁 **Category:** ").append(item.category()).append("\n");
        text.append("📅 **Year:** ").append(item.year()).append("\n\n");
        text.append("📝 **Overview:**\n").append(item.summary()).append("\n");

        if (item.facts() != null && !item.facts().isEmpty()) {
            text.append("\n⚙️ **Technical & Historical Details:**\n");
            for (String fact : item.facts()) {
                text.append(" • ").append(fact).append("\n");
            }
        }

        if (item.related() != null && !item.related().isEmpty()) {
            text.append("\n🔗 **Related 90s Topics:** ").append(String.join(", ", item.related())).append("\n");
        }

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("explanation", text.toString());
        result.put("topic", topic);
        return result;
    }

    private JsonNode executeTrivia(JsonNode args) {
        String category = getArgumentString(args, "category");
        List<KnowledgeItem> items;

        if (!category.isBlank()) {
            items = knowledgeService.findByCategory(category);
            if (items.isEmpty()) {
                items = knowledgeService.search(category);
            }
        } else {
            items = knowledgeService.getAllItems();
        }

        Optional<KnowledgeItem> selectedItem;
        if (!items.isEmpty()) {
            int idx = (int) (Math.random() * items.size());
            selectedItem = Optional.of(items.get(idx));
        } else {
            selectedItem = knowledgeService.getRandomItem();
        }

        KnowledgeItem item = selectedItem.orElseGet(() -> searchService.searchWithFallback("90s trivia"));

        String fact = (item.facts() != null && !item.facts().isEmpty())
            ? item.facts().get((int) (Math.random() * item.facts().size()))
            : item.summary();

        StringBuilder text = new StringBuilder();
        text.append("❓ **90s Cyber Trivia Time!** ❓\n\n");
        text.append("Did you know that in **").append(item.year()).append("**, regarding **").append(item.title()).append("**:\n\n");
        text.append("👉 \"").append(fact).append("\"\n\n");
        text.append("Category: ").append(item.category()).append(" | Subject: ").append(item.title());

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("trivia", text.toString());
        result.put("fact", fact);
        result.put("title", item.title());
        return result;
    }

    private JsonNode executeNostalgia(JsonNode args) {
        String theme = getArgumentString(args, "theme");
        if (theme.isBlank()) {
            theme = "90s Friday Night Vibes";
        }

        List<KnowledgeItem> all = knowledgeService.getAllItems();
        KnowledgeItem item1 = all.size() > 0 ? all.get((int) (Math.random() * all.size())) : searchService.searchWithFallback("90s games");
        KnowledgeItem item2 = all.size() > 1 ? all.get((int) (Math.random() * all.size())) : searchService.searchWithFallback("90s music");

        StringBuilder text = new StringBuilder();
        text.append("📼 **Cyber-Steve's Nostalgia Time Machine** 📼\n");
        text.append("Theme: ").append(theme).append("\n\n");
        text.append("Picture this: It's the mid-90s. The dial-up modem is screeching (CONNECT 28800!). ");
        text.append("You've got a cool can of Surge soda, neon windbreaker zipped up, and you're diving into **")
            .append(item1.title()).append("** (").append(item1.year()).append("). ").append(item1.summary()).append("\n\n");
        text.append("Meanwhile in pop culture, everyone is talking about **").append(item2.title()).append("** (")
            .append(item2.year()).append("). ").append(item2.summary()).append("\n\n");
        text.append("Booyah! That's all that and a bag of chips! 🍕🎮");

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("nostalgia", text.toString());
        result.put("theme", theme);
        return result;
    }

    private JsonNode executeYear(JsonNode args) {
        int yearVal = 1995;
        if (args != null && args.has("year") && args.get("year").isNumber()) {
            yearVal = args.get("year").asInt();
        } else if (args != null && args.has("year") && args.get("year").isTextual()) {
            try {
                yearVal = Integer.parseInt(args.get("year").asText());
            } catch (NumberFormatException ignored) {}
        }

        if (yearVal < 1990 || yearVal > 1999) {
            return createToolResult("Year must be in the 1990s decade (1990-1999)!", true);
        }

        List<KnowledgeItem> items = knowledgeService.findByYear(yearVal);

        StringBuilder text = new StringBuilder();
        text.append("📅 **1990s Time Capsule: ").append(yearVal).append("** 📅\n\n");

        if (items.isEmpty()) {
            text.append("No local indexed items found for ").append(yearVal).append(". Searching archives...\n");
            KnowledgeItem fallback = searchService.searchWithFallback("Year " + yearVal + " 1990s");
            items = List.of(fallback);
        }

        text.append("Found ").append(items.size()).append(" major 90s releases/milestones for ").append(yearVal).append(":\n\n");
        for (KnowledgeItem item : items) {
            text.append(" • **").append(item.title()).append("** [").append(item.category()).append("]: ")
                .append(item.summary()).append("\n");
        }

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("yearBreakdown", text.toString());
        result.put("year", yearVal);
        result.put("itemCount", items.size());
        return result;
    }

    private JsonNode executeWebsite(JsonNode args) {
        String name = getArgumentString(args, "name");
        if (name.isBlank()) {
            return createToolResult("Please provide a website or dot-com portal name!", true);
        }

        List<KnowledgeItem> internetItems = knowledgeService.findByCategory("internet");
        KnowledgeItem item = null;
        for (KnowledgeItem ki : internetItems) {
            if (ki.title().equalsIgnoreCase(name) || ki.id().equalsIgnoreCase(name) || ki.title().toLowerCase().contains(name.toLowerCase())) {
                item = ki;
                break;
            }
        }

        if (item == null) {
            List<KnowledgeItem> searchResults = knowledgeService.search(name);
            item = searchResults.isEmpty() ? searchService.searchWithFallback(name + " 90s website internet") : searchResults.get(0);
        }

        StringBuilder text = new StringBuilder();
        text.append("🌐 **Information Superhighway Explorer: ").append(item.title()).append("** 🌐\n\n");
        text.append("🔗 **URL:** `http://www.").append(sanitizeSlug(item.title())).append(".com`\n");
        text.append("📅 **Launch / Milestone Year:** ").append(item.year()).append("\n");
        text.append("🏭 **Creator / Provider:** ").append(item.manufacturer() != null ? item.manufacturer() : "Net Pioneers").append("\n\n");
        text.append("📝 **Site Overview:**\n").append(item.summary()).append("\n");

        if (item.facts() != null && !item.facts().isEmpty()) {
            text.append("\n💾 **Web Archive Notes:**\n");
            for (String fact : item.facts()) {
                text.append(" - ").append(fact).append("\n");
            }
        }

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("website", text.toString());
        result.put("siteName", item.title());
        return result;
    }

    private JsonNode executeHardware(JsonNode args) {
        String component = getArgumentString(args, "component");
        if (component.isBlank()) {
            return createToolResult("Please provide a hardware component or device name!", true);
        }

        List<KnowledgeItem> hwItems = knowledgeService.findByCategory("hardware");
        KnowledgeItem item = null;
        for (KnowledgeItem ki : hwItems) {
            if (ki.title().equalsIgnoreCase(component) || ki.id().equalsIgnoreCase(component) || ki.title().toLowerCase().contains(component.toLowerCase())) {
                item = ki;
                break;
            }
        }

        if (item == null) {
            List<KnowledgeItem> searchResults = knowledgeService.search(component);
            item = searchResults.isEmpty() ? searchService.searchWithFallback(component + " 90s hardware") : searchResults.get(0);
        }

        StringBuilder text = new StringBuilder();
        text.append("💻 **90s Hardware Spec Sheet: ").append(item.title()).append("** 💻\n\n");
        text.append("🏭 **Manufacturer:** ").append(item.manufacturer() != null ? item.manufacturer() : "N/A").append("\n");
        text.append("📅 **Release Year:** ").append(item.year()).append("\n");
        text.append("📁 **Category:** ").append(item.category()).append("\n\n");
        text.append("📝 **Technical Overview:**\n").append(item.summary()).append("\n");

        if (item.facts() != null && !item.facts().isEmpty()) {
            text.append("\n⚡ **Hardware Specs & Facts:**\n");
            for (String fact : item.facts()) {
                text.append(" • ").append(fact).append("\n");
            }
        }

        ObjectNode result = createToolResult(text.toString(), false);
        result.put("hardware", text.toString());
        result.put("componentName", item.title());
        return result;
    }

    private ObjectNode createToolResult(String textContent, boolean isError) {
        ObjectNode result = objectMapper.createObjectNode();
        ArrayNode content = objectMapper.createArrayNode();
        ObjectNode item = objectMapper.createObjectNode();
        item.put("type", "text");
        item.put("text", textContent);
        content.add(item);
        result.set("content", content);
        if (isError) {
            result.put("isError", true);
        }
        return result;
    }

    private ObjectNode createToolDefinition(String name, String description, ObjectNode inputSchema) {
        ObjectNode tool = objectMapper.createObjectNode();
        tool.put("name", name);
        tool.put("description", description);
        tool.set("inputSchema", inputSchema);
        return tool;
    }

    private ObjectNode createObjectSchema(Map<String, ObjectNode> properties, List<String> required) {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode propsNode = objectMapper.createObjectNode();
        properties.forEach(propsNode::set);
        schema.set("properties", propsNode);
        if (required != null && !required.isEmpty()) {
            ArrayNode reqArray = objectMapper.createArrayNode();
            required.forEach(reqArray::add);
            schema.set("required", reqArray);
        }
        return schema;
    }

    private ObjectNode createStringProperty(String description) {
        ObjectNode prop = objectMapper.createObjectNode();
        prop.put("type", "string");
        prop.put("description", description);
        return prop;
    }

    private ObjectNode createIntegerProperty(String description) {
        ObjectNode prop = objectMapper.createObjectNode();
        prop.put("type", "integer");
        prop.put("description", description);
        return prop;
    }

    private String getArgumentString(JsonNode args, String name) {
        if (args != null && args.has(name) && !args.get(name).isNull()) {
            return args.get(name).asText("").trim();
        }
        return "";
    }

    private String sanitizeSlug(String text) {
        if (text == null || text.isBlank()) {
            return "web";
        }
        return text.trim().toLowerCase()
            .replaceAll("[^a-z0-9]+", "")
            .replaceAll("^-+|-+$", "");
    }
}
