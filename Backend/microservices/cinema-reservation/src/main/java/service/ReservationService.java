package service;

import dto.ReservationRequestDTO;
import dto.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {
    ReservationResponseDTO create(ReservationRequestDTO request);
    ReservationResponseDTO findById(String id);
    List<ReservationResponseDTO> findAll();
    ReservationResponseDTO update(String id, ReservationRequestDTO request);
    void deleteById(String id);
}
