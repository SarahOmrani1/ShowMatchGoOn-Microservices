package entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    private String id;

    private Date dateReservation;
    private String numeroPlace;
    private String statut;
    private String contenuId;
    private String userId;
    private double prix;
    private String seanceId;
    private String salleId;
}
