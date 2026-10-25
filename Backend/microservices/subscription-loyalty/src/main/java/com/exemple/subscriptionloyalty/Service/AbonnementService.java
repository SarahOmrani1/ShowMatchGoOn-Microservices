package com.exemple.subscriptionloyalty.Service;


import com.exemple.subscriptionloyalty.Entity.Abonnement;

import java.util.List;

public interface AbonnementService {

    Abonnement addAbonnement(Abonnement a);

    List<Abonnement> getAll();

    Abonnement getById(String id);

    Abonnement update(String id, Abonnement a);

    void delete(String id);
}
