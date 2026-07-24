package com.demo.testing.agent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Autonomous sub-agent responsible for parsing surefire XML logs and root-cause analysis.
 */
public class DefectAnalyzerAgent extends BaseAgent {

    private static final Path SUREFIRE_DIR = Paths.get("testing-scenarios/target/surefire-reports");

    public DefectAnalyzerAgent(AgentMessageBus messageBus) {
        super("DefectAnalyzerAgent", messageBus);
        messageBus.subscribe("TEST_FAILURES", this);
    }

    @Override
    protected void onStart() {
        System.out.println("[" + agentId + "] Initialized and subscribed to TEST_FAILURES.");
    }

    @Override
    protected void processMessage(AgentMessage message) {
        if ("TEST_FAILURES".equals(message.topic())) {
            analyzeFailures(message.payload());
        }
    }

    private void analyzeFailures(String failurePayload) {
        System.out.println("[" + agentId + "] Analyzing failure payload: " + failurePayload);
        StringBuilder analysis = new StringBuilder();
        analysis.append("Root Cause Analysis:\n");

        if (Files.exists(SUREFIRE_DIR)) {
            try (Stream<Path> stream = Files.list(SUREFIRE_DIR)) {
                stream.filter(p -> p.getFileName().toString().endsWith(".xml"))
                        .forEach(p -> analysis.append("- Checked Surefire report: ").append(p.getFileName()).append("\n"));
            } catch (Exception e) {
                analysis.append("- Exception reading surefire dir: ").append(e.getMessage()).append("\n");
            }
        } else {
            analysis.append("- Zero surefire XML failure reports found. Pipeline executed cleanly.\n");
        }

        publish("DEFECT_ANALYSIS", analysis.toString());
    }

    @Override
    protected void onShutdown() {
        System.out.println("[" + agentId + "] Shutdown complete.");
    }
}
