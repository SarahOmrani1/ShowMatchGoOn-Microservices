package service.impI;

import dto.PromotionRequestDTO;
import dto.PromotionResponseDTO;
import entity.Promotion;
import repository.PromotionRepository;
import service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository repository;

    @Override
    public PromotionResponseDTO createPromotion(PromotionRequestDTO dto) {
        Promotion p = new Promotion();
        BeanUtils.copyProperties(dto, p);
        p.setActive(true);
        return toDto(repository.save(p));
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