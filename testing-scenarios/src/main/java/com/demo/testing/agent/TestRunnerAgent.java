package com.demo.testing.agent;

import com.demo.testing.MainTestPipeline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Pure Java Sub-Agent for executing automated testing scenarios, verifying MCP server
 * availability, and writing caveman-compressed markdown execution reports.
 */
public class TestRunnerAgent {

    private static final Logger LOGGER = Logger.getLogger(TestRunnerAgent.class.getName());
    
    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("user.dir")).getParent();
    private static final Path MCP_JAR = PROJECT_ROOT.resolve("mcp-server/target/mcp-server-1.0.0-SNAPSHOT-jar-with-dependencies.jar");
    private static final Path REPORTS_DIR = PROJECT_ROOT.resolve("testing-scenarios/reports");
    private static final Path SUMMARY_REPORT = PROJECT_ROOT.resolve("reports/agent_java_test_runner_summary.md");

    public static void main(String[] args) {
        TestRunnerAgent agent = new TestRunnerAgent();
        agent.run();
    }

    public void run() {
        logCaveman("Java TestRunnerAgent start.");
        long startTime = System.currentTimeMillis();

        boolean mcpAvailable = verifyMcpServerJar();
        boolean testSuccess = executeTestPipeline();

        long durationMs = System.currentTimeMillis() - startTime;
        writeCavemanSummaryReport(durationMs, testSuccess, mcpAvailable);

        if (testSuccess) {
            logCaveman("ALL TESTS PASSED CLEANLY.");
        } else {
            logCaveman("TEST PIPELINE FAILED.");
            System.exit(1);
        }
    }

    private boolean verifyMcpServerJar() {
        boolean exists = Files.exists(MCP_JAR);
        if (exists) {
            logCaveman("MCP Server FAT JAR verified: " + MCP_JAR.getFileName());
        } else {
            logCaveman("WARNING: MCP Server FAT JAR missing at " + MCP_JAR);
        }
        return exists;
    }

    private boolean executeTestPipeline() {
        logCaveman("Executing MainTestPipeline...");
        try {
            System.setProperty("headless", "true");
            MainTestPipeline.main(new String[0]);
            return true;
        } catch (Exception e) {
            logCaveman("ERROR executing MainTestPipeline: " + e.getMessage());
            return false;
        }
    }

    private void writeCavemanSummaryReport(long durationMs, boolean success, boolean mcpAvailable) {
        String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(Instant.now());

        String statusStr = success ? "PASSED" : "FAILED";
        double durationSec = durationMs / 1000.0;

        StringBuilder report = new StringBuilder();
        report.append("# Java Test Runner Agent Summary Report\n\n");
        report.append("- **Agent Implementation:** Pure Java (`com.demo.testing.agent.TestRunnerAgent`)\n");
        report.append("- **Timestamp:** ").append(timestamp).append("\n");
        report.append("- **Status:** ").append(statusStr).append("\n");
        report.append("- **Duration:** ").append(String.format("%.2f", durationSec)).append("s\n");
        report.append("- **MCP Server JAR Present:** ").append(mcpAvailable).append("\n\n");

        report.append("## Test Execution Details\n");
        report.append("- Suite: `com.demo.testing.MainTestPipeline`\n");
        report.append("- Mode: Headless Playwright\n");
        report.append("- Output Reports: `").append(REPORTS_DIR).append("`\n\n");

        report.append("## Caveman Summary\n");
        report.append("Java agent run complete. MainTestPipeline finish. Exit status: ").append(statusStr).append(". Zero shell/python scripts used. All reports written to reports/.\n");

        try {
            if (SUMMARY_REPORT.getParent() != null) {
                Files.createDirectories(SUMMARY_REPORT.getParent());
            }
            Files.writeString(SUMMARY_REPORT, report.toString());
            logCaveman("Caveman summary report saved: " + SUMMARY_REPORT);
        } catch (IOException e) {
            logCaveman("ERROR saving summary report: " + e.getMessage());
        }
    }

    private void logCaveman(String message) {
        System.out.println("[TestRunnerAgent] " + message);
    }
}
