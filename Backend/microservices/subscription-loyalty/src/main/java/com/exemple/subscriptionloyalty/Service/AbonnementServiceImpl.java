package com.exemple.subscriptionloyalty.Service;

import com.exemple.subscriptionloyalty.Entity.Abonnement;
import com.exemple.subscriptionloyalty.Repository.AbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbonnementServiceImpl implements AbonnementService {

    @Autowired
    private AbonnementRepository repo;

    public Abonnement addAbonnement(Abonnement a){
        return repo.save(a);
    }

    public List<Abonnement> getAll(){
        return repo.findAll();
    }

    public Abonnement getById(String id){
        return repo.findById(id).orElse(null);
    }

    public Abonnement update(String id, Abonnement a){
        a.setId(id);
        return repo.save(a);
    }

    public void delete(String id){
        repo.deleteById(id);
    }
}
