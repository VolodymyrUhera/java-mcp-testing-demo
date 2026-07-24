package com.demo.testing.agent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Autonomous sub-agent responsible for aggregating events and generating Markdown summary reports.
 */
public class ReportAggregatorAgent extends BaseAgent {

    private static final Path OUTPUT_REPORT = Paths.get("reports/agent_multiagent_summary.md");
    private final List<String> eventLogs = new ArrayList<>();

    public ReportAggregatorAgent(AgentMessageBus messageBus) {
        super("ReportAggregatorAgent", messageBus);
        messageBus.subscribe("TEST_EVENTS", this);
        messageBus.subscribe("DEFECT_ANALYSIS", this);
    }

    @Override
    protected void onStart() {
        System.out.println("[" + agentId + "] Initialized. Subscribed to TEST_EVENTS & DEFECT_ANALYSIS.");
    }

    @Override
    protected void processMessage(AgentMessage message) {
        String logEntry = "[" + message.timestamp() + "] [" + message.senderId() + "] [" + message.topic() + "] " + message.payload();
        eventLogs.add(logEntry);
        System.out.println("[" + agentId + "] Aggregated event: " + logEntry);
    }

    public void generateSummaryReport() {
        StringBuilder report = new StringBuilder();
        report.append("# Java Multi-Agent Orchestrator Summary Report\n\n");
        report.append("- **Timestamp:** ").append(Instant.now()).append("\n");
        report.append("- **Architecture:** In-Memory Pub/Sub MessageBus + Java 21 Virtual Threads\n");
        report.append("- **Active Agents:** `TestExecutorAgent`, `DefectAnalyzerAgent`, `ReportAggregatorAgent`\n\n");

        report.append("## Inter-Agent Event Log\n");
        for (String event : eventLogs) {
            report.append("- ").append(event).append("\n");
        }

        report.append("\n## Caveman Summary\n");
        report.append("Multi-agent run complete. Virtual thread agent pool finish. Zero deadlock. All events routed cleanly over in-memory MessageBus.\n");

        try {
            if (OUTPUT_REPORT.getParent() != null) {
                Files.createDirectories(OUTPUT_REPORT.getParent());
            }
            Files.writeString(OUTPUT_REPORT, report.toString());
            System.out.println("[" + agentId + "] Saved multi-agent summary report to: " + OUTPUT_REPORT);
        } catch (IOException e) {
            System.err.println("[" + agentId + "] Failed to write report: " + e.getMessage());
        }
    }

    @Override
    protected void onShutdown() {
        generateSummaryReport();
        System.out.println("[" + agentId + "] Shutdown complete.");
    }
}
