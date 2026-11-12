package com.exemple.subscriptionloyalty.Messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class PremiumEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final JsonMapper jsonMapper;

    public void publish(String routingKey, PremiumEvent event) {
        try {
            rabbitTemplate.convertAndSend(PremiumRabbitConfig.EXCHANGE, routingKey,
                    jsonMapper.writeValueAsString(event));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not publish premium event", exception);
        }
    }
}