package dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {
    private String id;
    private String seanceId;
    private Date dateReservation;
    private String numeroPlace;
    private String statut;
    private double prix;
    private String userId;
    private String contenuId;
    private String salleId;
}
