package com.exemple.subscriptionloyalty.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.exemple.subscriptionloyalty.Entity.Fidelity;

public interface FidelityRepository extends MongoRepository<Fidelity, String> {
}
