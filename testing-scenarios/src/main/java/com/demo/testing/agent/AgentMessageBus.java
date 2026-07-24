package com.demo.testing.agent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * In-memory thread-safe pub/sub message bus for inter-agent communication.
 */
public class AgentMessageBus {

    private static final Logger LOGGER = Logger.getLogger(AgentMessageBus.class.getName());

    private final Map<String, List<BaseAgent>> topicSubscribers = new ConcurrentHashMap<>();
    private final Map<String, BaseAgent> registeredAgents = new ConcurrentHashMap<>();

    public void registerAgent(BaseAgent agent) {
        registeredAgents.put(agent.getAgentId(), agent);
        LOGGER.info("[MessageBus] Registered agent: " + agent.getAgentId());
    }

    public void subscribe(String topic, BaseAgent agent) {
        topicSubscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(agent);
        LOGGER.info("[MessageBus] Agent " + agent.getAgentId() + " subscribed to topic: " + topic);
    }

    public void publish(AgentMessage message) {
        if (message.recipientId() != null && !message.recipientId().isEmpty() && !"*".equals(message.recipientId())) {
            BaseAgent target = registeredAgents.get(message.recipientId());
            if (target != null) {
                target.enqueueMessage(message);
            } else {
                LOGGER.warning("[MessageBus] Recipient not found: " + message.recipientId());
            }
            return;
        }

        List<BaseAgent> subscribers = topicSubscribers.get(message.topic());
        if (subscribers != null) {
            for (BaseAgent subscriber : subscribers) {
                subscriber.enqueueMessage(message);
            }
        }
    }
}
