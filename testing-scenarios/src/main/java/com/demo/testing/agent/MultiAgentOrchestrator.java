package com.demo.testing.agent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Main entry point for bootstrapping and orchestrating the Java 21 Multi-Agent System.
 */
public class MultiAgentOrchestrator {

    private static final Logger LOGGER = Logger.getLogger(MultiAgentOrchestrator.class.getName());

    public static void main(String[] args) {
        System.out.println("[Orchestrator] Bootstrapping Native Java 21 Multi-Agent System...");

        AgentMessageBus messageBus = new AgentMessageBus();

        TestExecutorAgent executorAgent = new TestExecutorAgent(messageBus);
        DefectAnalyzerAgent analyzerAgent = new DefectAnalyzerAgent(messageBus);
        ReportAggregatorAgent reportAgent = new ReportAggregatorAgent(messageBus);

        try (ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            virtualExecutor.submit(executorAgent);
            virtualExecutor.submit(analyzerAgent);
            virtualExecutor.submit(reportAgent);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("[Orchestrator] Triggering test execution workflow...");
            messageBus.publish(AgentMessage.create("Orchestrator", executorAgent.getAgentId(), "TRIGGER_TESTS", "START_SUITE"));

            try {
                Thread.sleep(22000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("[Orchestrator] Shutting down multi-agent system...");
            executorAgent.stop();
            analyzerAgent.stop();
            reportAgent.stop();

            virtualExecutor.shutdown();
            try {
                if (!virtualExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    virtualExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                virtualExecutor.shutdownNow();
            }
        }

        System.out.println("[Orchestrator] Multi-Agent System execution completed successfully.");
    }
}
