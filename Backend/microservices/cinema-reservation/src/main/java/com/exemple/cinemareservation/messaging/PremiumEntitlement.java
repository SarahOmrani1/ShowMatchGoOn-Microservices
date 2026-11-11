package com.exemple.cinemareservation.messaging;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "premium_entitlements")
public class PremiumEntitlement {

    @Id
    private String id;
    private Long userId;
    private String subscriptionId;
    private String status;
    private String benefit;
    private Instant activatedAt;

    public PremiumEntitlement() {
    }

    public PremiumEntitlement(String id, Long userId, String subscriptionId,
                               String status, String benefit, Instant activatedAt) {
        this.id = id;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.status = status;
        this.benefit = benefit;
        this.activatedAt = activatedAt;
    }

    public Long getUserId() { return userId; }
    public String getSubscriptionId() { return subscriptionId; }
    public String getStatus() { return status; }
    public String getBenefit() { return benefit; }
    public Instant getActivatedAt() { return activatedAt; }
}