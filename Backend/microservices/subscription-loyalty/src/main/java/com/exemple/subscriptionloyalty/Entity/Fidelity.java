package com.exemple.subscriptionloyalty.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

@Document(collection = "fidelities")
public class Fidelity {

    @Id
    private String id;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    @Positive(message = "L'identifiant de l'utilisateur doit être positif")
    private Long userId;

    @Min(value = 0, message = "Les points doivent être positifs")
    private int points;

    @NotNull(message = "Le niveau de fidélité est obligatoire")
    private FidelityLevel level;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 5, max = 300,
            message = "Description entre 5 et 300 caractères")
    private String description;

    public Fidelity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public FidelityLevel getLevel() { return level; }
    public void setLevel(FidelityLevel level) { this.level = level; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
