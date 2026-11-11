package com.exemple.subscriptionloyalty.Repository;

import com.exemple.subscriptionloyalty.Entity.PremiumSubscription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PremiumSubscriptionRepository extends MongoRepository<PremiumSubscription, String> {
}