package com.exemple.user.messaging;

import java.time.Instant;

public record PremiumEvent(
        String eventId,
        String correlationId,
        String subscriptionId,
        Long userId,
        String plan,
        String status,
        String message,
        Instant occurredAt) {
}