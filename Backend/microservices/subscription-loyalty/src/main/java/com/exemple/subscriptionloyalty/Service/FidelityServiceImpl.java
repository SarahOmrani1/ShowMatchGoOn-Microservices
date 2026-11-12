package com.exemple.subscriptionloyalty.Service;

import com.exemple.subscriptionloyalty.Entity.Fidelity;
import com.exemple.subscriptionloyalty.Repository.FidelityRepository;
import com.exemple.subscriptionloyalty.Client.UserClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FidelityServiceImpl implements FidelityService {

    @Autowired
    private FidelityRepository repo;

    @Autowired
    private UserClient userClient;

    public Fidelity addFidelity(Fidelity f) {
        try {
            userClient.getUserById(f.getUserId());
        } catch (FeignException.NotFound exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Utilisateur " + f.getUserId() + " introuvable", exception);
        } catch (FeignException | IllegalStateException exception) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Le service utilisateur est indisponible", exception);
        }
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