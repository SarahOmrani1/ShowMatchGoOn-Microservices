package controller;

import dto.PromotionRequestDTO;
import dto.PromotionResponseDTO;
import service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionResponseDTO> create(@Valid @RequestBody PromotionRequestDTO dto) {
        return ResponseEntity.status(201).body(promotionService.createPromotion(dto));
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponseDTO>> getAll() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponseDTO> update(@PathVariable String id,
                                                       @Valid @RequestBody PromotionRequestDTO dto) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.noContent().build();
    }
}