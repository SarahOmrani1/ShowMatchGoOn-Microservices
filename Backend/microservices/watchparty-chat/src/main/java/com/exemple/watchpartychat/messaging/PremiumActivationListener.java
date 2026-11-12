package com.exemple.watchpartychat.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class PremiumActivationListener {

    private final MongoTemplate mongoTemplate;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = PremiumRabbitConfig.ACTIVATED_QUEUE)
    public void activatePremiumWatchPartyBenefit(String payload) throws Exception {
        PremiumEvent event = jsonMapper.readValue(payload, PremiumEvent.class);
        if (!"ACTIVE".equals(event.status())) {
            return;
        }

        mongoTemplate.save(new PremiumEntitlement(String.valueOf(event.userId()), event.userId(),
                event.subscriptionId(), "ACTIVE", "CREATE_PRIVATE_WATCHPARTIES", event.occurredAt()));
        log.info("Premium watch-party benefit activated for user {}", event.userId());
    }
}