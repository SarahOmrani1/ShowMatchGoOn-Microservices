package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceRequestDTO {
    @NotNull(message = "dateSeance is required")
    private LocalDate dateSeance;

    @NotBlank(message = "heureSeance is required")
    private String heureSeance;

    @NotBlank(message = "salleId is required")
    private String salleId;

    @NotBlank(message = "cinemaId is required")
    private String cinemaId;

    private String contenuId;
}
