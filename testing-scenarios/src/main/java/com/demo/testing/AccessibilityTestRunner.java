package com.demo.testing;

import com.microsoft.playwright.Page;
import java.util.*;

public class AccessibilityTestRunner {
    private final String baseUrl;

    public AccessibilityTestRunner(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> runAccessibilityAudit(Page page) {
        Map<String, Object> audit = new LinkedHashMap<>();
        List<Map<String, Object>> pageAudits = new ArrayList<>();

        int totalDefectsFound = 0;
        String[] pages = {"/", "/about", "/contact", "/links"};

        for (String path : pages) {
            String fullUrl = baseUrl + path;
            page.navigate(fullUrl);

            String jsScript = """
                () => {
                    const defects = [];
                    
                    // 1. Check images for ALT attribute
                    const images = Array.from(document.querySelectorAll('img'));
                    images.forEach((img, idx) => {
                        if (!img.hasAttribute('alt') || img.getAttribute('alt').trim() === '') {
                            defects.push({
                                type: 'MISSING_ALT_TEXT',
                                selector: 'img[' + idx + ']',
                                src: img.getAttribute('src') || 'unknown',
                                severity: 'HIGH',
                                description: 'Image tag is missing required alt attribute for screen readers.'
                            });
                        }
                    });
                    
                    // 2. Check form inputs for labels
                    const inputs = Array.from(document.querySelectorAll('input, select, textarea'));
                    inputs.forEach(inp => {
                        if (inp.type === 'submit' || inp.type === 'button') return;
                        const id = inp.id;
                        const hasLabel = (id && document.querySelector(`label[for="${id}"]`)) || inp.closest('label');
                        if (!hasLabel) {
                            defects.push({
                                type: 'MISSING_FORM_LABEL',
                                selector: inp.name || inp.tagName,
                                severity: 'HIGH',
                                description: 'Form input missing associated <label> or aria-label.'
                            });
                        }
                    });
                    
                    // 3. Check heading hierarchy
                    const headings = Array.from(document.querySelectorAll('h1, h2, h3, h4, h5, h6')).map(h => ({
                        level: parseInt(h.tagName.substring(1)),
                        text: h.innerText
                    }));
                    for (let i = 0; i < headings.length - 1; i++) {
                        if (headings[i+1].level > headings[i].level + 1) {
                            defects.push({
                                type: 'HEADING_HIERARCHY_SKIP',
                                selector: 'H' + headings[i+1].level,
                                severity: 'MEDIUM',
                                description: 'Heading level jumped from H' + headings[i].level + ' directly to H' + headings[i+1].level
                            });
                        }
                    }

                    // 4. Check low contrast elements
                    const lowContrastEls = Array.from(document.querySelectorAll('.low-contrast-text'));
                    lowContrastEls.forEach(el => {
                        defects.push({
                            type: 'POOR_COLOR_CONTRAST',
                            selector: '.low-contrast-text',
                            severity: 'HIGH',
                            description: 'Foreground/background contrast ratio fails WCAG AA 4.5:1 minimum threshold.'
                        });
                    });

                    // 5. Check small click targets (< 24px)
                    const tinyLinks = Array.from(document.querySelectorAll('.tiny-link, .tiny-submit-btn'));
                    tinyLinks.forEach(el => {
                        defects.push({
                            type: 'SMALL_CLICK_TARGET',
                            selector: el.className,
                            severity: 'MEDIUM',
                            description: 'Touch/click target size is under 24px, causing touch navigation errors.'
                        });
                    });

                    return {
                        path: '""" + path + """
                        ',
                        defectCount: defects.length,
                        defects: defects
                    };
                }
                """;

            Object res = page.evaluate(jsScript);
            if (res instanceof Map) {
                Map<String, Object> mapRes = (Map<String, Object>) res;
                totalDefectsFound += ((Number) mapRes.getOrDefault("defectCount", 0)).intValue();
                pageAudits.add(mapRes);
            }
        }

        audit.put("totalDefectsFound", totalDefectsFound);
        audit.put("pageAudits", pageAudits);
        return audit;
    }
}
