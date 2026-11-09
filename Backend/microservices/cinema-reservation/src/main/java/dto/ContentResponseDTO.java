package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContentResponseDTO {

    private String id;
    private String title;
    private String contentType;
}
