package dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleResponseDTO {
    private String id;
    private String name;
    private int capacity;
    private int rowCount;
    private int seatsPerRow;
}
