package com.demo.mcp.tools;

import com.demo.mcp.json.JsonValue;
import java.util.*;

public class PlaywrightToolRegistry {
    private final PlaywrightManager playwrightManager;

    public PlaywrightToolRegistry(PlaywrightManager playwrightManager) {
        this.playwrightManager = playwrightManager;
    }

    public List<JsonValue> getToolDefinitions() {
        List<JsonValue> tools = new ArrayList<>();
        
        tools.add(createToolDef("launch_browser", "Launch Playwright browser instance", 
            Map.of("headless", Map.of("type", "boolean", "description", "Run headless mode"))));
            
        tools.add(createToolDef("close_browser", "Close active Playwright browser instance", Map.of()));
        
        tools.add(createToolDef("open_url", "Navigate browser to specified URL", 
            Map.of("url", Map.of("type", "string", "description", "Target URL"))));
            
        tools.add(createToolDef("navigate", "Navigate page to specified URL", 
            Map.of("url", Map.of("type", "string", "description", "Target URL"))));
            
        tools.add(createToolDef("click", "Click element matching selector", 
            Map.of("selector", Map.of("type", "string", "description", "CSS Selector"))));
            
        tools.add(createToolDef("fill_form", "Fill input element matching selector", 
            Map.of("selector", Map.of("type", "string", "description", "CSS Selector"),
                   "value", Map.of("type", "string", "description", "Input Value"))));
                   
        tools.add(createToolDef("wait_for_selector", "Wait for element selector to appear", 
            Map.of("selector", Map.of("type", "string", "description", "CSS Selector"),
                   "timeout", Map.of("type", "number", "description", "Timeout in ms"))));
                   
        tools.add(createToolDef("evaluate_javascript", "Execute custom JS on current page", 
            Map.of("script", Map.of("type", "string", "description", "JavaScript snippet"))));
            
        tools.add(createToolDef("take_screenshot", "Capture full page screenshot to file", 
            Map.of("filePath", Map.of("type", "string", "description", "Output path"))));
            
        tools.add(createToolDef("save_pdf", "Save current page as PDF file", 
            Map.of("filePath", Map.of("type", "string", "description", "Output path"))));
            
        tools.add(createToolDef("extract_content", "Extract page title, text, and link elements", Map.of()));
        
        tools.add(createToolDef("get_performance_metrics", "Collect page load times, paint metrics, resource counts", Map.of()));
        
        tools.add(createToolDef("analyze_accessibility", "Run DOM accessibility audit for ALT tags, labels, contrast, headers", Map.of()));

        return tools;
    }

    private JsonValue createToolDef(String name, String description, Map<String, Object> props) {
        Map<String, JsonValue> map = new LinkedHashMap<>();
        map.put("name", JsonValue.of(name));
        map.put("description", JsonValue.of(description));
        
        Map<String, JsonValue> schema = new LinkedHashMap<>();
        schema.put("type", JsonValue.of("object"));
        
        Map<String, JsonValue> propsMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            @SuppressWarnings("unchecked")
            Map<String, String> propDef = (Map<String, String>) entry.getValue();
            Map<String, JsonValue> pMap = new LinkedHashMap<>();
            pMap.put("type", JsonValue.of(propDef.get("type")));
            pMap.put("description", JsonValue.of(propDef.get("description")));
            propsMap.put(entry.getKey(), JsonValue.of(pMap));
        }
        schema.put("properties", JsonValue.of(propsMap));
        map.put("inputSchema", JsonValue.of(schema));
        return JsonValue.of(map);
    }

    public String executeTool(String name, JsonValue args) {
        switch (name) {
            case "launch_browser" -> {
                boolean headless = args != null && args.get("headless") != null && args.get("headless").asBoolean();
                playwrightManager.launchBrowser(headless);
                return "Browser launched successfully.";
            }
            case "close_browser" -> {
                playwrightManager.closeBrowser();
                return "Browser closed successfully.";
            }
            case "open_url", "navigate" -> {
                String url = args != null && args.get("url") != null ? args.get("url").asString() : "http://localhost:8080";
                return playwrightManager.navigate(url);
            }
            case "click" -> {
                String selector = args != null && args.get("selector") != null ? args.get("selector").asString() : "";
                return playwrightManager.click(selector);
            }
            case "fill_form" -> {
                String selector = args != null && args.get("selector") != null ? args.get("selector").asString() : "";
                String value = args != null && args.get("value") != null ? args.get("value").asString() : "";
                return playwrightManager.fillForm(selector, value);
            }
            case "wait_for_selector" -> {
                String selector = args != null && args.get("selector") != null ? args.get("selector").asString() : "";
                double timeout = args != null && args.get("timeout") != null ? args.get("timeout").asDouble() : 5000.0;
                return playwrightManager.waitForSelector(selector, timeout);
            }
            case "evaluate_javascript" -> {
                String script = args != null && args.get("script") != null ? args.get("script").asString() : "";
                Object res = playwrightManager.evaluateJavascript(script);
                return "Evaluation Result: " + res;
            }
            case "take_screenshot" -> {
                String path = args != null && args.get("filePath") != null ? args.get("filePath").asString() : "reports/screenshot.png";
                return playwrightManager.takeScreenshot(path);
            }
            case "save_pdf" -> {
                String path = args != null && args.get("filePath") != null ? args.get("filePath").asString() : "reports/page.pdf";
                return playwrightManager.savePdf(path);
            }
            case "extract_content" -> {
                Map<String, Object> content = playwrightManager.extractContent();
                return "Extracted Content: " + content.toString();
            }
            case "get_performance_metrics" -> {
                Map<String, Object> metrics = playwrightManager.getPerformanceMetrics();
                return "Performance Metrics: " + metrics.toString();
            }
            case "analyze_accessibility" -> {
                Map<String, Object> a11y = playwrightManager.analyzeAccessibility();
                return "Accessibility Analysis: " + a11y.toString();
            }
            default -> throw new IllegalArgumentException("Unknown tool: " + name);
        }
    }
}
