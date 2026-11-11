package com.exemple.subscriptionloyalty.Service;

import com.exemple.subscriptionloyalty.Entity.PremiumSubscription;
import com.exemple.subscriptionloyalty.Messaging.PremiumEvent;
import com.exemple.subscriptionloyalty.Messaging.PremiumEventPublisher;
import com.exemple.subscriptionloyalty.Messaging.PremiumRabbitConfig;
import com.exemple.subscriptionloyalty.Repository.PremiumSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PremiumSubscriptionService {

    private final PremiumSubscriptionRepository repository;
    private final PremiumEventPublisher eventPublisher;

    public PremiumSubscription requestPremium(Long userId) {
        PremiumSubscription subscription = new PremiumSubscription();
        subscription.setUserId(userId);
        subscription.setPlan("PREMIUM");
        subscription.setStatus("PENDING");
        subscription.setMessage("Validation utilisateur en attente");
        subscription.setCreatedAt(Instant.now());
        subscription.setUpdatedAt(Instant.now());
        PremiumSubscription saved = repository.save(subscription);

        eventPublisher.publish(PremiumRabbitConfig.REQUESTED_KEY, new PremiumEvent(
                UUID.randomUUID().toString(), saved.getId(), saved.getId(), userId,
                "PREMIUM", "REQUESTED", "Validation utilisateur demandee", Instant.now()));
        return saved;
    }

    public PremiumSubscription findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void handleValidation(PremiumEvent event, boolean validated) {
        PremiumSubscription subscription = repository.findById(event.subscriptionId()).orElse(null);
        if (subscription == null || !"PENDING".equals(subscription.getStatus())) {
            return;
        }

        Instant now = Instant.now();
        subscription.setStatus(validated ? "ACTIVE" : "REJECTED");
        subscription.setMessage(event.message());
        subscription.setUpdatedAt(now);
        repository.save(subscription);

        if (validated) {
            eventPublisher.publish(PremiumRabbitConfig.ACTIVATED_KEY, new PremiumEvent(
                    UUID.randomUUID().toString(), event.correlationId(), subscription.getId(),
                    subscription.getUserId(), subscription.getPlan(), "ACTIVE",
                    "Souscription Premium activee", now));
        }
    }
}