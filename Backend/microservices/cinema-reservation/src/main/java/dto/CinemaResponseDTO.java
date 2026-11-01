package dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaResponseDTO {
    private String id;
    private String nom;
    private String adresse;
    private String ville;
    private Double latitude;
    private Double longitude;
}
