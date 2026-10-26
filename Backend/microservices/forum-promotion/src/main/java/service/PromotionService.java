package service;

import dto.PromotionRequestDTO;
import dto.PromotionResponseDTO;

import java.util.List;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionRequestDTO dto);
    List<PromotionResponseDTO> getAllPromotions();
    PromotionResponseDTO getPromotionById(String id);
    PromotionResponseDTO updatePromotion(String id, PromotionRequestDTO dto);
    void deletePromotion(String id);
}