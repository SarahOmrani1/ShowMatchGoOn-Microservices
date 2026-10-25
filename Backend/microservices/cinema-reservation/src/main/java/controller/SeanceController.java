package controller;

import dto.SeanceRequestDTO;
import dto.SeanceResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SeanceService;

import java.util.List;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceController {

    private final SeanceService seanceService;

    @PostMapping
    public ResponseEntity<SeanceResponseDTO> create(@Valid @RequestBody SeanceRequestDTO request) {
        return new ResponseEntity<>(seanceService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeanceResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(seanceService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<SeanceResponseDTO>> findAll() {
        return ResponseEntity.ok(seanceService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeanceResponseDTO> update(@PathVariable String id, @Valid @RequestBody SeanceRequestDTO request) {
        return ResponseEntity.ok(seanceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        seanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
