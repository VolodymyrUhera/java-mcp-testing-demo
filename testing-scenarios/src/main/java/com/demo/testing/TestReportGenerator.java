package com.demo.testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TestReportGenerator {

    @SuppressWarnings("unchecked")
    public static void generateReports(Map<String, Object> functionalResults,
                                       Map<String, Object> performanceResults,
                                       Map<String, Object> accessibilityAudit,
                                       Map<String, Object> uxJourneyResults) throws IOException {

        Files.createDirectories(Paths.get("reports"));

        // 1. Functional Report Markdown
        StringBuilder funcMd = new StringBuilder("# Functional Testing & Resource Audit Report\n\n");
        funcMd.append("- **Passed Checks:** ").append(functionalResults.get("passedCount")).append("\n");
        funcMd.append("- **Failed Checks:** ").append(functionalResults.get("failedCount")).append("\n\n");
        
        funcMd.append("## Route Availability Checks\n\n");
        funcMd.append("| Path | Target URL | HTTP Status | Test Result |\n");
        funcMd.append("|------|------------|-------------|-------------|\n");

        List<Map<String, Object>> pageChecks = (List<Map<String, Object>>) functionalResults.get("pageChecks");
        for (Map<String, Object> check : pageChecks) {
            funcMd.append("| ").append(check.get("path")).append(" | ")
                  .append(check.get("url")).append(" | ")
                  .append(check.get("statusCode")).append(" | ")
                  .append(check.get("status")).append(" |\n");
        }

        Map<String, Object> formTest = (Map<String, Object>) functionalResults.get("formTest");
        funcMd.append("\n## Form Submissions\n\n");
        funcMd.append("- **Test:** ").append(formTest.get("test")).append(" -> **Status:** ").append(formTest.get("status")).append("\n\n");

        // Broken Links Audit Section
        List<Map<String, Object>> brokenLinks = (List<Map<String, Object>>) functionalResults.getOrDefault("brokenLinks", Collections.emptyList());
        funcMd.append("## Broken Links Audit\n\n");
        if (brokenLinks.isEmpty()) {
            funcMd.append("✅ **No broken links detected on any page.**\n\n");
        } else {
            funcMd.append("⚠️ **Found ").append(brokenLinks.size()).append(" broken link(s):**\n\n");
            funcMd.append("| Source Page | Link Text | Target URL | Issue / Error |\n");
            funcMd.append("|-------------|-----------|------------|--------------|\n");
            for (Map<String, Object> link : brokenLinks) {
                String statusOrErr = link.containsKey("statusCode") ? "HTTP " + link.get("statusCode") : String.valueOf(link.get("error"));
                funcMd.append("| ").append(link.get("page")).append(" | ")
                      .append(link.get("linkText")).append(" | ")
                      .append(link.get("url")).append(" | ")
                      .append(statusOrErr).append(" |\n");
            }
            funcMd.append("\n");
        }

        // Broken Images Audit Section
        List<Map<String, Object>> brokenImages = (List<Map<String, Object>>) functionalResults.getOrDefault("brokenImages", Collections.emptyList());
        funcMd.append("## Broken Images Audit\n\n");
        if (brokenImages.isEmpty()) {
            funcMd.append("✅ **No broken images detected on any page.**\n\n");
        } else {
            funcMd.append("⚠️ **Found ").append(brokenImages.size()).append(" broken image(s):**\n\n");
            funcMd.append("| Source Page | Image Src | Reason |\n");
            funcMd.append("|-------------|-----------|--------|\n");
            for (Map<String, Object> img : brokenImages) {
                funcMd.append("| ").append(img.get("page")).append(" | ")
                      .append(img.get("src")).append(" | ")
                      .append(img.get("reason")).append(" |\n");
            }
            funcMd.append("\n");
        }

        Files.writeString(Paths.get("reports/functional_report.md"), funcMd.toString());

        // 2. Performance Report Markdown
        StringBuilder perfMd = new StringBuilder("# Performance Testing Report\n\n");
        perfMd.append("| Page Path | Load Time (ms) | DOM Content Loaded (ms) | FCP (ms) | Total Resources |\n");
        perfMd.append("|-----------|----------------|--------------------------|----------|-----------------|\n");
        List<Map<String, Object>> perfMetrics = (List<Map<String, Object>>) performanceResults.get("metricsPerPage");
        for (Map<String, Object> metric : perfMetrics) {
            perfMd.append("| ").append(metric.get("path")).append(" | ")
                  .append(metric.get("loadTimeMs")).append(" | ")
                  .append(metric.get("domContentLoadedMs")).append(" | ")
                  .append(metric.get("firstContentfulPaintMs")).append(" | ")
                  .append(metric.get("resourceCount")).append(" |\n");
        }
        Files.writeString(Paths.get("reports/performance_report.md"), perfMd.toString());

        // 3. Accessibility Report Markdown
        StringBuilder a11yMd = new StringBuilder("# Accessibility Audit Report\n\n");
        a11yMd.append("- **Total WCAG / DOM Defects Identified:** ").append(accessibilityAudit.get("totalDefectsFound")).append("\n\n");
        a11yMd.append("## Identified Defect Log\n\n");
        a11yMd.append("| Page Path | Defect Type | Severity | Description |\n");
        a11yMd.append("|-----------|-------------|----------|-------------|\n");
        List<Map<String, Object>> pageAudits = (List<Map<String, Object>>) accessibilityAudit.get("pageAudits");
        for (Map<String, Object> pageAudit : pageAudits) {
            String path = (String) pageAudit.get("path");
            List<Map<String, Object>> defects = (List<Map<String, Object>>) pageAudit.get("defects");
            for (Map<String, Object> d : defects) {
                a11yMd.append("| ").append(path).append(" | ")
                      .append(d.get("type")).append(" | ")
                      .append(d.get("severity")).append(" | ")
                      .append(d.get("description")).append(" |\n");
            }
        }
        Files.writeString(Paths.get("reports/accessibility_report.md"), a11yMd.toString());

        // 4. UX Journey Report Markdown
        StringBuilder uxMd = new StringBuilder("# UX Journey Test Report\n\n");
        uxMd.append("- **Journey:** ").append(uxJourneyResults.get("journeyName")).append("\n");
        uxMd.append("- **Total Duration:** ").append(uxJourneyResults.get("totalDurationMs")).append(" ms\n");
        uxMd.append("- **Total Interactions:** ").append(uxJourneyResults.get("interactionCount")).append("\n");
        uxMd.append("- **Failures:** ").append(uxJourneyResults.get("failures")).append("\n\n");
        uxMd.append("## Journey Steps Execution Log\n\n");
        uxMd.append("| Step | Target / Artifact | Status | Latency (ms) |\n");
        uxMd.append("|------|-------------------|--------|--------------|\n");
        List<Map<String, Object>> steps = (List<Map<String, Object>>) uxJourneyResults.get("steps");
        for (Map<String, Object> step : steps) {
            uxMd.append("| ").append(step.get("stepName")).append(" | ")
                 .append(step.get("target")).append(" | ")
                 .append(step.get("status")).append(" | ")
                 .append(step.get("durationMs")).append(" |\n");
        }
        uxMd.append("\n![UX Journey Proof Screenshot](ux_journey_screenshot.png)\n");
        Files.writeString(Paths.get("reports/ux_journey_report.md"), uxMd.toString());
    }
}
