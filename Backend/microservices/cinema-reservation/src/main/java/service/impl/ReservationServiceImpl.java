package service.impl;

import dto.ReservationRequestDTO;
import dto.ReservationResponseDTO;
import entity.Reservation;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.ReservationRepository;
import service.ReservationService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO request) {
        Reservation reservation = Reservation.builder()
                .seanceId(request.getSeanceId())
                .userId(request.getUserId())
                .numeroPlace(request.getNumeroPlace())
                .prix(request.getPrix())
                .contenuId(request.getContenuId())
                .salleId(request.getSalleId())
                .dateReservation(new Date())
                .statut("CONFIRMEE")
                .build();
        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponseDTO findById(String id) {
        return toResponse(getReservation(id));
    }

    @Override
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ReservationResponseDTO update(String id, ReservationRequestDTO request) {
        Reservation reservation = getReservation(id);
        reservation.setSeanceId(request.getSeanceId());
        reservation.setUserId(request.getUserId());
        reservation.setNumeroPlace(request.getNumeroPlace());
        reservation.setPrix(request.getPrix());
        reservation.setContenuId(request.getContenuId());
        reservation.setSalleId(request.getSalleId());
        return toResponse(reservationRepository.save(reservation));
    }

    @Override
    public void deleteById(String id) {
        reservationRepository.delete(getReservation(id));
    }

    private Reservation getReservation(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
    }

    private ReservationResponseDTO toResponse(Reservation reservation) {
        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .seanceId(reservation.getSeanceId())
                .dateReservation(reservation.getDateReservation())
                .numeroPlace(reservation.getNumeroPlace())
                .statut(reservation.getStatut())
                .prix(reservation.getPrix())
                .userId(reservation.getUserId())
                .contenuId(reservation.getContenuId())
                .salleId(reservation.getSalleId())
                .build();
    }
}
