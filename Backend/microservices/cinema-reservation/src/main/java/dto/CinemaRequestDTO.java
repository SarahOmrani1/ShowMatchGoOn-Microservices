package dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CinemaRequestDTO {
    @NotBlank(message = "nom is required")
    private String nom;

    @NotBlank(message = "adresse is required")
    private String adresse;

    @NotBlank(message = "ville is required")
    private String ville;

    private Double latitude;
    private Double longitude;
}
