package com.demo.testing.agent;

import com.demo.testing.MainTestPipeline;

/**
 * Autonomous sub-agent responsible for triggering test executions and broadcasting events.
 */
public class TestExecutorAgent extends BaseAgent {

    public TestExecutorAgent(AgentMessageBus messageBus) {
        super("TestExecutorAgent", messageBus);
    }

    @Override
    protected void onStart() {
        System.out.println("[" + agentId + "] Initialized. Listening for execution trigger...");
    }

    @Override
    protected void processMessage(AgentMessage message) {
        if ("TRIGGER_TESTS".equals(message.topic())) {
            executeTestPipeline();
        }
    }

    private void executeTestPipeline() {
        System.out.println("[" + agentId + "] Executing MainTestPipeline suite...");
        publish("TEST_EVENTS", "START: MainTestPipeline suite execution started.");

        long startTime = System.currentTimeMillis();
        try {
            System.setProperty("headless", "true");
            MainTestPipeline.main(new String[0]);
            long duration = System.currentTimeMillis() - startTime;
            publish("TEST_EVENTS", "SUCCESS: MainTestPipeline executed in " + duration + "ms.");
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            publish("TEST_FAILURES", "FAILURE: MainTestPipeline failed after " + duration + "ms. Error: " + e.getMessage());
        }
    }

    @Override
    protected void onShutdown() {
        System.out.println("[" + agentId + "] Shutdown complete.");
    }
}
