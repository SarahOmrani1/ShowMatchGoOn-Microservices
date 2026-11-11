package com.exemple.contentnotification.messaging;

import com.exemple.contentnotification.Entity.Notification;
import com.exemple.contentnotification.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PremiumActivationListener {

    private final NotificationRepository notificationRepository;
    private final JsonMapper jsonMapper;

    @RabbitListener(queues = PremiumRabbitConfig.ACTIVATED_QUEUE)
    public void notifyPremiumActivation(String payload) throws Exception {
        PremiumEvent event = jsonMapper.readValue(payload, PremiumEvent.class);
        if (!"ACTIVE".equals(event.status())) {
            return;
        }

        String notificationId = "premium-" + event.subscriptionId();
        if (!notificationRepository.existsById(notificationId)) {
            notificationRepository.save(Notification.builder()
                    .id(notificationId)
                    .title("Abonnement Premium active")
                    .message("Votre abonnement Premium est actif. Vos avantages sont disponibles.")
                    .type("SUCCESS")
                    .createdAt(LocalDateTime.now())
                    .userId(String.valueOf(event.userId()))
                    .username("user-" + event.userId())
                    .build());
        }
        log.info("Premium activation notification saved for user {}", event.userId());
    }
}