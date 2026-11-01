package service.impl;

import dto.CinemaRequestDTO;
import dto.CinemaResponseDTO;
import entity.Cinema;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.CinemaRepository;
import service.CinemaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;

    @Override
    public CinemaResponseDTO create(CinemaRequestDTO request) {
        Cinema cinema = Cinema.builder()
                .nom(request.getNom())
                .adresse(request.getAdresse())
                .ville(request.getVille())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        return toResponse(cinemaRepository.save(cinema));
    }

    @Override
    public CinemaResponseDTO findById(String id) {
        return toResponse(getCinema(id));
    }

    @Override
    public List<CinemaResponseDTO> findAll() {
        return cinemaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CinemaResponseDTO update(String id, CinemaRequestDTO request) {
        Cinema cinema = getCinema(id);
        cinema.setNom(request.getNom());
        cinema.setAdresse(request.getAdresse());
        cinema.setVille(request.getVille());
        cinema.setLatitude(request.getLatitude());
        cinema.setLongitude(request.getLongitude());
        return toResponse(cinemaRepository.save(cinema));
    }

    @Override
    public void deleteById(String id) {
        cinemaRepository.delete(getCinema(id));
    }

    private Cinema getCinema(String id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with id: " + id));
    }

    private CinemaResponseDTO toResponse(Cinema cinema) {
        return CinemaResponseDTO.builder()
                .id(cinema.getId())
                .nom(cinema.getNom())
                .adresse(cinema.getAdresse())
                .ville(cinema.getVille())
                .latitude(cinema.getLatitude())
                .longitude(cinema.getLongitude())
                .build();
    }
}
