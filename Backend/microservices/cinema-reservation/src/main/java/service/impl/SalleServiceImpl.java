package service.impl;

import dto.SalleRequestDTO;
import dto.SalleResponseDTO;
import entity.Salle;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.SalleRepository;
import service.SalleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;

    @Override
    public SalleResponseDTO create(SalleRequestDTO request) {
        Salle salle = Salle.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .rowCount(request.getRowCount() != null ? request.getRowCount() : 0)
                .seatsPerRow(request.getSeatsPerRow() != null ? request.getSeatsPerRow() : 0)
                .build();
        return toResponse(salleRepository.save(salle));
    }

    @Override
    public SalleResponseDTO findById(String id) {
        return toResponse(getSalle(id));
    }

    @Override
    public List<SalleResponseDTO> findAll() {
        return salleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SalleResponseDTO update(String id, SalleRequestDTO request) {
        Salle salle = getSalle(id);
        salle.setName(request.getName());
        salle.setCapacity(request.getCapacity());
        salle.setRowCount(request.getRowCount() != null ? request.getRowCount() : 0);
        salle.setSeatsPerRow(request.getSeatsPerRow() != null ? request.getSeatsPerRow() : 0);
        return toResponse(salleRepository.save(salle));
    }

    @Override
    public void deleteById(String id) {
        salleRepository.delete(getSalle(id));
    }

    private Salle getSalle(String id) {
        return salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle not found with id: " + id));
    }

    private SalleResponseDTO toResponse(Salle salle) {
        return SalleResponseDTO.builder()
                .id(salle.getId())
                .name(salle.getName())
                .capacity(salle.getCapacity())
                .rowCount(salle.getRowCount())
                .seatsPerRow(salle.getSeatsPerRow())
                .build();
    }
}
