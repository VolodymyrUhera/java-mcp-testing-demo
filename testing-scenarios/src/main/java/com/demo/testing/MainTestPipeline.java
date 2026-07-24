package com.demo.testing;

import com.demo.website.DemoWebServer;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.util.Map;
import java.util.logging.Logger;

public class MainTestPipeline {
    private static final Logger LOGGER = Logger.getLogger(MainTestPipeline.class.getName());
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://localhost:" + PORT;

    public static void main(String[] args) {
        LOGGER.info("Starting Automated Test Execution Pipeline...");

        DemoWebServer server = null;
        Playwright playwright = null;
        Browser browser = null;

        try {
            // 1. Launch Demo Web Server (or reuse if already running)
            try {
                server = new DemoWebServer(PORT);
                server.start();
                LOGGER.info("Server active at " + BASE_URL);
            } catch (Exception serverException) {
                LOGGER.warning("DemoWebServer already running or port bound: " + serverException.getMessage() + ". Reusing active server at " + BASE_URL);
            }

            // 2. Launch Playwright Browser (Supports -Dheadless=true/false)
            boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
            double slowMo = Double.parseDouble(System.getProperty("slowMo", "500"));
            LOGGER.info("Launching Playwright Browser (Headless: " + headless + ", SlowMo delay: " + slowMo + "ms)...");
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless).setSlowMo(slowMo));
            Page page = browser.newPage();

            // 3. Execute Functional Tests
            LOGGER.info("Running Functional Tests...");
            FunctionalTestRunner functionalRunner = new FunctionalTestRunner(BASE_URL);
            Map<String, Object> functionalResults = functionalRunner.runFunctionalTests(page);

            // 4. Execute Performance Tests
            LOGGER.info("Running Performance Tests...");
            PerformanceTestRunner performanceRunner = new PerformanceTestRunner(BASE_URL);
            Map<String, Object> performanceResults = performanceRunner.runPerformanceTests(page);

            // 5. Execute Accessibility Audit
            LOGGER.info("Running Accessibility Audit...");
            AccessibilityTestRunner accessibilityRunner = new AccessibilityTestRunner(BASE_URL);
            Map<String, Object> accessibilityAudit = accessibilityRunner.runAccessibilityAudit(page);

            // 6. Execute UX Journey Test
            LOGGER.info("Running UX Journey Test...");
            UxJourneyTestRunner uxRunner = new UxJourneyTestRunner(BASE_URL);
            Map<String, Object> uxResults = uxRunner.runUxJourney(page);

            // 7. Generate Reports
            LOGGER.info("Generating Test Reports in reports/ directory...");
            TestReportGenerator.generateReports(functionalResults, performanceResults, accessibilityAudit, uxResults);

            LOGGER.info("Automated Test Execution Pipeline Completed Successfully!");
        } catch (Exception e) {
            LOGGER.severe("Test Execution Pipeline failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (browser != null) browser.close();
            if (playwright != null) playwright.close();
            if (server != null) server.stop();
        }
    }
}
