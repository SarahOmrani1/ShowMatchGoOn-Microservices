package com.exemple.subscriptionloyalty.Controller;
import com.exemple.subscriptionloyalty.Entity.Abonnement;
import com.exemple.subscriptionloyalty.Service.AbonnementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/abonnements")
@CrossOrigin("*")
public class AbonnementController {

    @Autowired
    private AbonnementService service;

    @PostMapping
    public Abonnement create(@Valid @RequestBody Abonnement abonnement) {
        return service.addAbonnement(abonnement);
    }

    @GetMapping
    public List<Abonnement> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Abonnement getById(@PathVariable String id) {
        Abonnement abonnement = service.getById(id);
        if (abonnement == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Abonnement introuvable");
        }
        return abonnement;
    }

    @PutMapping("/{id}")
    public Abonnement update(@PathVariable String id, @Valid @RequestBody Abonnement abonnement) {
        abonnement.setId(id);
        return service.update(id, abonnement);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {

        service.delete(id);

        return "Abonnement supprimé avec succès";
    }
}
