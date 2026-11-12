package com.exemple.forumpromotion.service.impI;

import com.exemple.forumpromotion.client.NotificationClient;
import com.exemple.forumpromotion.dto.NotificationRequestDTO;
import com.exemple.forumpromotion.dto.PromotionRequestDTO;
import com.exemple.forumpromotion.dto.PromotionResponseDTO;
import com.exemple.forumpromotion.entity.Promotion;
import com.exemple.forumpromotion.repository.PromotionRepository;
import com.exemple.forumpromotion.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpI implements PromotionService {

    private final PromotionRepository repository;
    private final NotificationClient notificationClient;

    @Value("${eureka.client.service-url.defaultZone}")
    private String eurekaUrl;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequestDTO dto) {
        // 1. Vérifie qu'Eureka répond et que content-notification y est enregistré
        assertEurekaUp();

        Promotion p = new Promotion();
        BeanUtils.copyProperties(dto, p);
        p.setActive(true);
        Promotion saved = repository.save(p);

        // 2. Appel synchrone : on attend la réponse de content-notification
        try {
            NotificationRequestDTO notif = NotificationRequestDTO.builder()
                    .title("Nouvelle promotion")
                    .message("La promotion " + saved.getCode() + " est disponible : -"
                            + saved.getPourcentageReduction() + "%")
                    .type("INFO")
                    .userId(saved.getClientId() != null ? saved.getClientId() : "ALL")
                    .build();
            notificationClient.createNotification(notif);
        } catch (Exception e) {
            // Annulation : la promotion ne doit pas rester en base si la notification échoue
            repository.deleteById(saved.getId());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Notification impossible, promotion annulée : " + e.getMessage());
        }

        return toDto(saved);
    }

    @Override
    public List<PromotionResponseDTO> getAllPromotions() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public PromotionResponseDTO getPromotionById(String id) {
        return toDto(find(id));
    }

    @Override
    public PromotionResponseDTO updatePromotion(String id, PromotionRequestDTO dto) {
        Promotion p = find(id);
        BeanUtils.copyProperties(dto, p, "id", "active");
        return toDto(repository.save(p));
    }

    @Override
    public void deletePromotion(String id) {
        repository.delete(find(id));
    }

    private void assertEurekaUp() {
        try {
            RestClient.create().get()
                    .uri(eurekaUrl + "/apps/CONTENT-NOTIFICATION")
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Eureka indisponible ou content-notification non enregistré");
        }
    }

    private Promotion find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable : " + id));
    }

    private PromotionResponseDTO toDto(Promotion p) {
        PromotionResponseDTO dto = new PromotionResponseDTO();
        BeanUtils.copyProperties(p, dto);
        return dto;
    }
}