package service.impl;

import client.ContentClient;
import dto.ReservationRequestDTO;
import dto.ReservationResponseDTO;
import entity.Salle;
import entity.Seance;
import entity.Reservation;
import exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import feign.FeignException;
import org.springframework.stereotype.Service;
import repository.ReservationRepository;
import repository.SalleRepository;
import repository.SeanceRepository;
import service.ReservationService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final SalleRepository salleRepository;
    private final SeanceRepository seanceRepository;
    private final ContentClient contentClient;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO request) {
        validateContent(request.getContenuId());
        Seance seance = getSeance(request.getSeanceId());
        validateSeanceContent(seance, request.getContenuId());
        Salle salle = getSalle(request.getSalleId() != null ? request.getSalleId() : seance.getSalle());
        validateSeatAvailability(request, salle);

        Reservation reservation = Reservation.builder()
                .seanceId(request.getSeanceId())
                .userId(request.getUserId())
                .numeroPlace(request.getNumeroPlace())
                .prix(request.getPrix())
                .contenuId(request.getContenuId())
                .salleId(salle.getId())
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

    private Seance getSeance(String id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seance not found with id: " + id));
    }

    private Salle getSalle(String id) {
        if (id == null || id.isBlank()) {
            throw new ResourceNotFoundException("Salle is required for the seance");
        }
        return salleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salle not found with id: " + id));
    }

    private void validateContent(String contentId) {
        if (contentId == null || contentId.isBlank()) {
            throw new ResourceNotFoundException("Content id is required");
        }
        try {
            contentClient.getContentById(contentId);
        } catch (FeignException.NotFound exception) {
            throw new ResourceNotFoundException("Content not found with id: " + contentId);
        }
    }

    private void validateSeanceContent(Seance seance, String contentId) {
        if (seance.getContenuId() != null && !contentId.equals(seance.getContenuId())) {
            throw new ResourceNotFoundException("Content does not match the selected seance");
        }
    }

    private void validateSeatAvailability(ReservationRequestDTO request, Salle salle) {
        if (reservationRepository.existsBySeanceIdAndNumeroPlace(
                request.getSeanceId(), request.getNumeroPlace())) {
            throw new IllegalStateException("Seat is already reserved for this seance");
        }
        if (reservationRepository.countBySeanceId(request.getSeanceId()) >= salle.getCapacity()) {
            throw new IllegalStateException("No seats available for this seance");
        }
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
