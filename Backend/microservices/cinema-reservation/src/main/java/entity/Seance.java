package entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "seances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seance {

    @Id
    private String id;

    private LocalDate dateSeance;
    private String heureSeance;

    private String salle;
    private String cinemaId;
    private String contenuId;
}
