package service;

import dto.CinemaRequestDTO;
import dto.CinemaResponseDTO;

import java.util.List;

public interface CinemaService {
    CinemaResponseDTO create(CinemaRequestDTO request);
    CinemaResponseDTO findById(String id);
    List<CinemaResponseDTO> findAll();
    CinemaResponseDTO update(String id, CinemaRequestDTO request);
    void deleteById(String id);
}
