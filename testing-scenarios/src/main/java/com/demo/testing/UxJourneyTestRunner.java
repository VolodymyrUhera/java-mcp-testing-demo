package com.demo.testing;

import com.microsoft.playwright.Page;
import java.nio.file.Paths;
import java.util.*;

public class UxJourneyTestRunner {
    private final String baseUrl;

    public UxJourneyTestRunner(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> runUxJourney(Page page) {
        Map<String, Object> journey = new LinkedHashMap<>();
        List<Map<String, Object>> steps = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        int interactionCount = 0;
        int failures = 0;

        // Step 1: Open Home Page
        long t0 = System.currentTimeMillis();
        page.navigate(baseUrl + "/");
        long t1 = System.currentTimeMillis();
        steps.add(createStep("1. Open Home Page", baseUrl + "/", "Success", t1 - t0));
        interactionCount++;

        // Step 2: Navigate to About Us
        t0 = System.currentTimeMillis();
        page.click("a[href='/about']");
        t1 = System.currentTimeMillis();
        steps.add(createStep("2. Click About Us Link", page.url(), "Success", t1 - t0));
        interactionCount++;

        // Step 3: Return to Home Page
        t0 = System.currentTimeMillis();
        page.click("a[href='/']");
        t1 = System.currentTimeMillis();
        steps.add(createStep("3. Return to Home Page", page.url(), "Success", t1 - t0));
        interactionCount++;

        // Step 4: Navigate to Contact Page
        t0 = System.currentTimeMillis();
        page.click("a[href='/contact']");
        t1 = System.currentTimeMillis();
        steps.add(createStep("4. Click Secret Portal (Contact)", page.url(), "Success", t1 - t0));
        interactionCount++;

        // Step 5: Fill Contact Form
        t0 = System.currentTimeMillis();
        page.fill("input[name='username']", "QA_Automation_User");
        page.fill("input[name='email']", "qa@testing.com");
        page.fill("textarea[name='comments']", "Evaluating UX journey performance and accessibility compliance.");
        t1 = System.currentTimeMillis();
        steps.add(createStep("5. Fill Contact Form Fields", page.url(), "Success", t1 - t0));
        interactionCount += 3;

        // Step 6: Submit Form via Tiny Button
        t0 = System.currentTimeMillis();
        page.waitForNavigation(() -> page.click("#submitBtn"));
        t1 = System.currentTimeMillis();
        boolean submitted = page.content().contains("Electronic Mail Sent!");
        steps.add(createStep("6. Submit Form", page.url(), submitted ? "Success" : "Failed", t1 - t0));
        interactionCount++;
        if (!submitted) failures++;

        // Step 7: Capture Screenshot
        String screenshotPath = "reports/ux_journey_screenshot.png";
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)).setFullPage(true));
        steps.add(createStep("7. Capture Proof Screenshot", screenshotPath, "Saved", 0));

        long totalDurationMs = System.currentTimeMillis() - startTime;

        journey.put("journeyName", "Home -> About -> Home -> Contact -> Submit Form -> Screenshot");
        journey.put("totalDurationMs", totalDurationMs);
        journey.put("interactionCount", interactionCount);
        journey.put("failures", failures);
        journey.put("steps", steps);

        return journey;
    }

    private Map<String, Object> createStep(String name, String target, String status, long durationMs) {
        Map<String, Object> step = new LinkedHashMap<>();
        step.put("stepName", name);
        step.put("target", target);
        step.put("status", status);
        step.put("durationMs", durationMs);
        return step;
    }
}
