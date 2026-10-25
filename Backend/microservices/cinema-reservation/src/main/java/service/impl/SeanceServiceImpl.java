package service.impl;

import dto.SeanceRequestDTO;
import dto.SeanceResponseDTO;
import entity.Seance;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.SeanceRepository;
import service.SeanceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;

    @Override
    public SeanceResponseDTO create(SeanceRequestDTO request) {
        Seance seance = Seance.builder()
                .dateSeance(request.getDateSeance())
                .heureSeance(request.getHeureSeance())
                .salle(request.getSalleId())
                .cinemaId(request.getCinemaId())
                .contenuId(request.getContenuId())
                .build();
        return toResponse(seanceRepository.save(seance));
    }

    @Override
    public SeanceResponseDTO findById(String id) {
        return toResponse(getSeance(id));
    }

    @Override
    public List<SeanceResponseDTO> findAll() {
        return seanceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SeanceResponseDTO update(String id, SeanceRequestDTO request) {
        Seance seance = getSeance(id);
        seance.setDateSeance(request.getDateSeance());
        seance.setHeureSeance(request.getHeureSeance());
        seance.setSalle(request.getSalleId());
        seance.setCinemaId(request.getCinemaId());
        seance.setContenuId(request.getContenuId());
        return toResponse(seanceRepository.save(seance));
    }

    @Override
    public void deleteById(String id) {
        seanceRepository.delete(getSeance(id));
    }

    private Seance getSeance(String id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found with id: " + id));
    }

    private SeanceResponseDTO toResponse(Seance seance) {
        return SeanceResponseDTO.builder()
                .id(seance.getId())
                .dateSeance(seance.getDateSeance())
                .heureSeance(seance.getHeureSeance())
                .salleId(seance.getSalle())
                .cinemaId(seance.getCinemaId())
                .contenuId(seance.getContenuId())
                .build();
    }
}
