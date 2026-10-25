package com.exemple.subscriptionloyalty.Repository;


import com.exemple.subscriptionloyalty.Entity.Abonnement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AbonnementRepository
        extends MongoRepository<Abonnement, String> {
}

