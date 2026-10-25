package com.exemple.subscriptionloyalty.Service;

import com.exemple.subscriptionloyalty.Entity.Fidelity;

import java.util.List;

public interface FidelityService {

    Fidelity addFidelity(Fidelity f);

    List<Fidelity> getAll();

    Fidelity getById(String id);

    Fidelity update(String id, Fidelity f);

    void delete(String id);
}