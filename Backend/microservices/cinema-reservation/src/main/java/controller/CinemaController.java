package controller;

import dto.CinemaRequestDTO;
import dto.CinemaResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CinemaService;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @PostMapping
    public ResponseEntity<CinemaResponseDTO> create(@Valid @RequestBody CinemaRequestDTO request) {
        return new ResponseEntity<>(cinemaService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(cinemaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CinemaResponseDTO>> findAll() {
        return ResponseEntity.ok(cinemaService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CinemaResponseDTO> update(@PathVariable String id, @Valid @RequestBody CinemaRequestDTO request) {
        return ResponseEntity.ok(cinemaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        cinemaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
