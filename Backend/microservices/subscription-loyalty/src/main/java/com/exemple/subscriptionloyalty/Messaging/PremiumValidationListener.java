package com.exemple.subscriptionloyalty.Messaging;

import com.exemple.subscriptionloyalty.Service.PremiumSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class PremiumValidationListener {

    private final PremiumSubscriptionService subscriptionService;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = PremiumRabbitConfig.VALIDATED_QUEUE)
    public void onUserValidated(String payload) throws Exception {
        subscriptionService.handleValidation(jsonMapper.readValue(payload, PremiumEvent.class), true);
    }

    @RabbitListener(queues = PremiumRabbitConfig.REJECTED_QUEUE)
    public void onUserRejected(String payload) throws Exception {
        subscriptionService.handleValidation(jsonMapper.readValue(payload, PremiumEvent.class), false);
    }
}