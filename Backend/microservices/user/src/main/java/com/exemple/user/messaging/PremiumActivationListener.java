package com.exemple.user.messaging;

import com.exemple.user.entity.User;
import com.exemple.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PremiumActivationListener {

    private final UserRepository userRepository;
    private final PremiumEventPublisher eventPublisher;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = PremiumRabbitConfig.REQUEST_QUEUE)
    public void validatePremiumActivation(String payload) throws Exception {
        PremiumEvent request = jsonMapper.readValue(payload, PremiumEvent.class);
        User user = userRepository.findById(request.userId()).orElse(null);
        boolean eligible = user != null && user.isEnabled() && !user.isLocked()
                && "ACTIVE".equalsIgnoreCase(user.getStatus());

        if (eligible) {
            user.setPremiumActive(true);
            user.setPremiumSubscriptionId(request.subscriptionId());
            userRepository.save(user);
        }

        String routingKey = eligible
                ? PremiumRabbitConfig.VALIDATED_KEY
                : PremiumRabbitConfig.REJECTED_KEY;
        String result = eligible ? "VALIDATED" : "REJECTED";
        String message = eligible ? "Utilisateur valide" : "Utilisateur absent ou inactif";
        eventPublisher.publish(routingKey, new PremiumEvent(
                UUID.randomUUID().toString(), request.correlationId(), request.subscriptionId(),
                request.userId(), request.plan(), result, message, Instant.now()));
        log.info("Premium request {} for user {}: {}", request.subscriptionId(), request.userId(), result);
    }
}