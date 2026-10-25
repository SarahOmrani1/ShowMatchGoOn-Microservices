package com.exemple.subscriptionloyalty.Entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "abonnements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abonnement {

    @Id
    private String id;

    @NotNull(message = "Le type d'abonnement est obligatoire")
    private AbonnementType type;

    @Positive(message = "Le prix doit être positif")
    private double prix;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 500, message = "La description doit contenir entre 10 et 500 caractères")
    private String description;
}