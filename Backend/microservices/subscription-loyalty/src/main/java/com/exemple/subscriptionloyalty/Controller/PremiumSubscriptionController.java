package com.exemple.subscriptionloyalty.Controller;

import com.exemple.subscriptionloyalty.Entity.PremiumSubscription;
import com.exemple.subscriptionloyalty.Service.PremiumSubscriptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/subscriptions/premium")
public class PremiumSubscriptionController {

    private final PremiumSubscriptionService subscriptionService;

    public PremiumSubscriptionController(PremiumSubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PremiumSubscription requestPremium(@Valid @RequestBody PremiumRequest request) {
        return subscriptionService.requestPremium(request.userId());
    }

    @GetMapping("/{id}")
    public PremiumSubscription getStatus(@PathVariable String id) {
        PremiumSubscription subscription = subscriptionService.findById(id);
        if (subscription == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Souscription introuvable");
        }
        return subscription;
    }

    public record PremiumRequest(@NotNull @Positive Long userId) {
    }
}