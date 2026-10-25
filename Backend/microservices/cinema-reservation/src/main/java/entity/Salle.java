package entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "salles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salle {

    @Id
    private String id;

    private String name;
    private int capacity;
    private int rowCount;
    private int seatsPerRow;
}
