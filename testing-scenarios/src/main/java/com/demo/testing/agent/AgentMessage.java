package com.demo.testing.agent;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable record representing an inter-agent message payload.
 */
public record AgentMessage(
        String id,
        String senderId,
        String recipientId,
        String topic,
        String payload,
        Instant timestamp
) {
    public static AgentMessage create(String senderId, String recipientId, String topic, String payload) {
        return new AgentMessage(
                UUID.randomUUID().toString(),
                senderId,
                recipientId,
                topic,
                payload,
                Instant.now()
        );
    }
}
