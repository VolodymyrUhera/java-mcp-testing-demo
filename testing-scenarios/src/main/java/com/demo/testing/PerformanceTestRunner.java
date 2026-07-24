package com.demo.testing;

import com.microsoft.playwright.Page;
import java.util.*;

public class PerformanceTestRunner {
    private final String baseUrl;

    public PerformanceTestRunner(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> runPerformanceTests(Page page) {
        Map<String, Object> results = new LinkedHashMap<>();
        List<Map<String, Object>> pageMetrics = new ArrayList<>();

        String[] pages = {"/", "/about", "/contact", "/links"};
        for (String path : pages) {
            String fullUrl = baseUrl + path;
            page.navigate(fullUrl);

            String jsScript = """
                () => {
                    const nav = performance.getEntriesByType('navigation')[0] || {};
                    const paints = performance.getEntriesByType('paint') || [];
                    let fcp = 0;
                    paints.forEach(p => { if (p.name === 'first-contentful-paint') fcp = p.startTime; });
                    
                    const resources = performance.getEntriesByType('resource') || [];
                    const slowResources = resources.filter(r => r.duration > 100).map(r => r.name);
                    
                    return {
                        path: '""" + path + """
                        ',
                        loadTimeMs: nav.loadEventEnd ? Math.round(nav.loadEventEnd - nav.startTime) : 0,
                        domContentLoadedMs: nav.domContentLoadedEventEnd ? Math.round(nav.domContentLoadedEventEnd - nav.startTime) : 0,
                        firstContentfulPaintMs: Math.round(fcp),
                        resourceCount: resources.length,
                        slowResourceCount: slowResources.length
                    };
                }
                """;

            Object res = page.evaluate(jsScript);
            if (res instanceof Map) {
                pageMetrics.add((Map<String, Object>) res);
            }
        }

        results.put("metricsPerPage", pageMetrics);
        return results;
    }
}
