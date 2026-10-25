package com.exemple.subscriptionloyalty.Service;

import com.exemple.subscriptionloyalty.Entity.Fidelity;
import com.exemple.subscriptionloyalty.Repository.FidelityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FidelityServiceImpl implements FidelityService {

    @Autowired
    private FidelityRepository repo;

    public Fidelity addFidelity(Fidelity f) {
        return repo.save(f);
    }

    public List<Fidelity> getAll() {
        return repo.findAll();
    }

    public Fidelity getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public Fidelity update(String id, Fidelity f) {
        f.setId(id);
        return repo.save(f);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}