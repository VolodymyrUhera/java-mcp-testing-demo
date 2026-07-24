package com.demo.testing;

import com.microsoft.playwright.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class FunctionalTestRunner {
    private final String baseUrl;

    public FunctionalTestRunner(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> runFunctionalTests(Page page) {
        Map<String, Object> results = new LinkedHashMap<>();
        List<Map<String, Object>> pageChecks = new ArrayList<>();
        List<Map<String, Object>> brokenLinks = new ArrayList<>();
        List<Map<String, Object>> brokenImages = new ArrayList<>();
        
        String[] paths = {"/", "/about", "/contact", "/links"};
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        int passedCount = 0;
        int failedCount = 0;

        for (String path : paths) {
            String fullUrl = baseUrl + path;
            Map<String, Object> check = new LinkedHashMap<>();
            check.put("path", path);
            check.put("url", fullUrl);

            try {
                // 1. Check HTTP status via HttpClient
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl)).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                check.put("statusCode", response.statusCode());
                boolean ok = response.statusCode() == 200;
                check.put("status", ok ? "PASSED" : "FAILED");
                
                if (ok) passedCount++;
                else failedCount++;

                // 2. Navigate in Playwright
                page.navigate(fullUrl);
                check.put("pageTitle", page.title());

                // 3. Scan & test all <a> links on the page
                List<ElementHandle> linkEls = page.querySelectorAll("a");
                for (ElementHandle el : linkEls) {
                    String href = el.getAttribute("href");
                    String text = el.innerText();
                    if (href == null || href.isEmpty() || href.startsWith("javascript:")) continue;

                    String targetUrl = href.startsWith("http") ? href : (baseUrl + (href.startsWith("/") ? href : "/" + href));
                    try {
                        HttpRequest linkReq = HttpRequest.newBuilder()
                                .uri(URI.create(targetUrl))
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                                .timeout(Duration.ofSeconds(4))
                                .GET()
                                .build();
                        HttpResponse<Void> linkRes = client.send(linkReq, HttpResponse.BodyHandlers.discarding());
                        if (linkRes.statusCode() >= 400) {
                            Map<String, Object> broken = new LinkedHashMap<>();
                            broken.put("page", path);
                            broken.put("linkText", text);
                            broken.put("url", targetUrl);
                            broken.put("statusCode", linkRes.statusCode());
                            brokenLinks.add(broken);
                        }
                    } catch (Exception e) {
                        Map<String, Object> broken = new LinkedHashMap<>();
                        broken.put("page", path);
                        broken.put("linkText", text);
                        broken.put("url", targetUrl);
                        broken.put("error", e.getMessage());
                        brokenLinks.add(broken);
                    }
                }

                // 4. Scan & test all <img> elements on the page (Check naturalWidth & HTTP status)
                String imgScript = """
                    () => {
                        const imgs = Array.from(document.querySelectorAll('img'));
                        return imgs.map(img => ({
                            src: img.getAttribute('src') || '',
                            loaded: img.complete && img.naturalWidth > 0
                        }));
                    }
                    """;
                Object imgResult = page.evaluate(imgScript);
                if (imgResult instanceof List) {
                    List<Map<String, Object>> imgs = (List<Map<String, Object>>) imgResult;
                    for (Map<String, Object> imgMap : imgs) {
                        String src = (String) imgMap.get("src");
                        boolean loaded = Boolean.TRUE.equals(imgMap.get("loaded"));
                        if (!loaded) {
                            Map<String, Object> brokenImg = new LinkedHashMap<>();
                            brokenImg.put("page", path);
                            brokenImg.put("src", src);
                            brokenImg.put("reason", "Image failed to render (naturalWidth == 0 or 404)");
                            brokenImages.add(brokenImg);
                        }
                    }
                }

            } catch (Exception e) {
                check.put("status", "FAILED");
                check.put("error", e.getMessage());
                failedCount++;
            }
            pageChecks.add(check);
        }

        // Test Contact Form POST Submission
        Map<String, Object> formTest = new LinkedHashMap<>();
        try {
            page.navigate(baseUrl + "/contact");
            page.fill("input[name='username']", "RetroGamer99");
            page.fill("input[name='email']", "gamer@geocities.com");
            page.fill("textarea[name='comments']", "Cool site! Add more animated GIFs please!");
            page.waitForNavigation(() -> page.click("#submitBtn"));

            boolean submissionSuccess = page.content().contains("Electronic Mail Sent!");
            formTest.put("test", "Contact Form Submission");
            formTest.put("status", submissionSuccess ? "PASSED" : "FAILED");
            if (submissionSuccess) passedCount++; else failedCount++;
        } catch (Exception e) {
            formTest.put("test", "Contact Form Submission");
            formTest.put("status", "FAILED");
            formTest.put("error", e.getMessage());
            failedCount++;
        }

        results.put("passedCount", passedCount);
        results.put("failedCount", failedCount);
        results.put("pageChecks", pageChecks);
        results.put("formTest", formTest);
        results.put("brokenLinks", brokenLinks);
        results.put("brokenImages", brokenImages);

        return results;
    }
}
