package com.demo.mcp.tools;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class PlaywrightManager {
    private static final Logger LOGGER = Logger.getLogger(PlaywrightManager.class.getName());

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public synchronized void launchBrowser(boolean headless) {
        if (playwright == null) {
            playwright = Playwright.create();
        }
        if (browser == null) {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            context = browser.newContext();
            page = context.newPage();
            LOGGER.info("Playwright Chromium browser launched successfully.");
        }
    }

    public synchronized void closeBrowser() {
        if (page != null) { page.close(); page = null; }
        if (context != null) { context.close(); context = null; }
        if (browser != null) { browser.close(); browser = null; }
        if (playwright != null) { playwright.close(); playwright = null; }
        LOGGER.info("Playwright browser closed.");
    }

    public Page getPage() {
        if (page == null) {
            launchBrowser(true);
        }
        return page;
    }

    public String navigate(String url) {
        Page p = getPage();
        Response res = p.navigate(url);
        int status = res != null ? res.status() : 0;
        return "Navigated to " + url + " [Status: " + status + "]";
    }

    public String click(String selector) {
        Page p = getPage();
        p.click(selector);
        return "Clicked element matching selector: " + selector;
    }

    public String fillForm(String selector, String value) {
        Page p = getPage();
        p.fill(selector, value);
        return "Filled selector '" + selector + "' with value '" + value + "'";
    }

    public String waitForSelector(String selector, double timeoutMs) {
        Page p = getPage();
        p.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeoutMs));
        return "Element matching '" + selector + "' is ready.";
    }

    public Object evaluateJavascript(String script) {
        Page p = getPage();
        return p.evaluate(script);
    }

    public String takeScreenshot(String filePath) {
        Page p = getPage();
        p.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)).setFullPage(true));
        return "Screenshot saved to " + filePath;
    }

    public String savePdf(String filePath) {
        Page p = getPage();
        p.pdf(new Page.PdfOptions().setPath(Paths.get(filePath)));
        return "PDF saved to " + filePath;
    }

    public Map<String, Object> extractContent() {
        Page p = getPage();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("title", p.title());
        map.put("url", p.url());
        
        List<ElementHandle> linkElements = p.querySelectorAll("a");
        List<Map<String, String>> links = new ArrayList<>();
        for (ElementHandle el : linkElements) {
            Map<String, String> linkMap = new LinkedHashMap<>();
            linkMap.put("text", el.innerText());
            linkMap.put("href", el.getAttribute("href"));
            links.add(linkMap);
        }
        map.put("links", links);
        map.put("bodyText", p.innerText("body"));
        return map;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getPerformanceMetrics() {
        Page p = getPage();
        String jsScript = """
            () => {
                const nav = performance.getEntriesByType('navigation')[0] || {};
                const paints = performance.getEntriesByType('paint') || [];
                let fcp = 0;
                paints.forEach(p => { if (p.name === 'first-contentful-paint') fcp = p.startTime; });
                
                return {
                    loadTimeMs: nav.loadEventEnd ? (nav.loadEventEnd - nav.startTime) : 0,
                    domContentLoadedMs: nav.domContentLoadedEventEnd ? (nav.domContentLoadedEventEnd - nav.startTime) : 0,
                    firstContentfulPaintMs: fcp,
                    resourceCount: performance.getEntriesByType('resource').length
                };
            }
            """;
        Object result = p.evaluate(jsScript);
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }
        return Map.of();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> analyzeAccessibility() {
        Page p = getPage();
        String jsScript = """
            () => {
                const images = Array.from(document.querySelectorAll('img'));
                const missingAltCount = images.filter(img => !img.hasAttribute('alt') || img.getAttribute('alt').trim() === '').length;
                
                const inputs = Array.from(document.querySelectorAll('input, select, textarea'));
                const unlabelledInputCount = inputs.filter(inp => {
                    if (inp.type === 'submit' || inp.type === 'button') return false;
                    const id = inp.id;
                    if (id && document.querySelector(`label[for="${id}"]`)) return false;
                    if (inp.closest('label')) return false;
                    return true;
                }).length;
                
                const headings = Array.from(document.querySelectorAll('h1, h2, h3, h4, h5, h6')).map(h => parseInt(h.tagName.substring(1)));
                let headingJumps = 0;
                for (let i = 0; i < headings.length - 1; i++) {
                    if (headings[i+1] > headings[i] + 1) {
                        headingJumps++;
                    }
                }
                
                return {
                    totalImages: images.length,
                    missingAltCount: missingAltCount,
                    totalFormInputs: inputs.length,
                    unlabelledInputCount: unlabelledInputCount,
                    headingJumps: headingJumps,
                    headingHierarchy: headings
                };
            }
            """;
        Object result = p.evaluate(jsScript);
        if (result instanceof Map) {
            return (Map<String, Object>) result;
        }
        return Map.of();
    }
}
