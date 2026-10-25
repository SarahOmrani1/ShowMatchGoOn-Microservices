package controller;

import dto.SalleRequestDTO;
import dto.SalleResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SalleService;

import java.util.List;

@RestController
@RequestMapping("/api/salles")
@RequiredArgsConstructor
public class SalleController {

    private final SalleService salleService;

    @PostMapping
    public ResponseEntity<SalleResponseDTO> create(@Valid @RequestBody SalleRequestDTO request) {
        return new ResponseEntity<>(salleService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalleResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(salleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SalleResponseDTO>> findAll() {
        return ResponseEntity.ok(salleService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalleResponseDTO> update(@PathVariable String id, @Valid @RequestBody SalleRequestDTO request) {
        return ResponseEntity.ok(salleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        salleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
