package com.demo.testing.agent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base class for autonomous Java agents running on Virtual Threads.
 */
public abstract class BaseAgent implements Runnable {

    protected final String agentId;
    protected final AgentMessageBus messageBus;
    protected final Queue<AgentMessage> inbox = new ConcurrentLinkedQueue<>();
    protected final AtomicBoolean running = new AtomicBoolean(true);

    public BaseAgent(String agentId, AgentMessageBus messageBus) {
        this.agentId = agentId;
        this.messageBus = messageBus;
        this.messageBus.registerAgent(this);
    }

    public String getAgentId() {
        return agentId;
    }

    public void enqueueMessage(AgentMessage message) {
        inbox.offer(message);
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        onStart();
        while (running.get() || !inbox.isEmpty()) {
            AgentMessage msg = inbox.poll();
            if (msg != null) {
                try {
                    processMessage(msg);
                } catch (Exception e) {
                    onException(msg, e);
                }
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        onShutdown();
    }

    protected abstract void onStart();
    protected abstract void processMessage(AgentMessage message);
    protected abstract void onShutdown();

    protected void onException(AgentMessage msg, Exception e) {
        System.err.println("[" + agentId + "] Error processing message " + msg.id() + ": " + e.getMessage());
    }

    protected void publish(String topic, String payload) {
        messageBus.publish(AgentMessage.create(agentId, "*", topic, payload));
    }

    protected void sendTo(String recipientId, String topic, String payload) {
        messageBus.publish(AgentMessage.create(agentId, recipientId, topic, payload));
    }
}
