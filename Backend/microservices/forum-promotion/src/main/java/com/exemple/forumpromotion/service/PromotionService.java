package com.exemple.forumpromotion.service;

import com.exemple.forumpromotion.dto.PromotionRequestDTO;
import com.exemple.forumpromotion.dto.PromotionResponseDTO;

import java.util.List;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionRequestDTO dto);
    List<PromotionResponseDTO> getAllPromotions();
    PromotionResponseDTO getPromotionById(String id);
    PromotionResponseDTO updatePromotion(String id, PromotionRequestDTO dto);
    void deletePromotion(String id);
}