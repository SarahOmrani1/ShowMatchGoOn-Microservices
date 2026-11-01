package com.exemple.subscriptionloyalty.Controller;


import com.exemple.subscriptionloyalty.Entity.Fidelity;
import com.exemple.subscriptionloyalty.Service.FidelityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/fidelities")
@CrossOrigin("*")
public class FidelityController {

	@Autowired
	private FidelityService service;

	@PostMapping
	public Fidelity create(@Valid @RequestBody Fidelity fidelity) {
		return service.addFidelity(fidelity);
	}

	@GetMapping
	public List<Fidelity> getAll() {
		return service.getAll();
	}

	@GetMapping("/{id}")
	public Fidelity getById(@PathVariable String id) {
		Fidelity fidelity = service.getById(id);
		if (fidelity == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fidélité introuvable");
		}
		return fidelity;
	}

	@PutMapping("/{id}")
	public Fidelity update(@PathVariable String id, @Valid @RequestBody Fidelity fidelity) {
		fidelity.setId(id);
		return service.update(id, fidelity);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		service.delete(id);
	}
}
