package com.exemple.cinemareservation.messaging;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestController
@RequestMapping("/api/cinema/premium/entitlements")
public class PremiumEntitlementController {

    private final MongoTemplate mongoTemplate;

    public PremiumEntitlementController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/{userId}")
    public PremiumEntitlementResponse getByUserId(@PathVariable Long userId) {
        PremiumEntitlement entitlement = mongoTemplate.findById(
                String.valueOf(userId), PremiumEntitlement.class);
        if (entitlement == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Avantage Premium cinema introuvable pour cet utilisateur");
        }
        return new PremiumEntitlementResponse(entitlement.getUserId(), entitlement.getSubscriptionId(),
                entitlement.getStatus(), entitlement.getBenefit(), entitlement.getActivatedAt());
    }

    public record PremiumEntitlementResponse(Long userId, String subscriptionId,
                                             String status, String benefit, Instant activatedAt) {
    }
}