package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleRequestDTO {
    @NotBlank(message = "name is required")
    private String name;

    @Positive(message = "capacity must be positive")
    private int capacity;

    private Integer rowCount;
    private Integer seatsPerRow;
}
