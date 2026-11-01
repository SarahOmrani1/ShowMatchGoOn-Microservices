package com.exemple.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String photoUrl;

    // Utilisé seulement à la création / modification
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String status;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    // Remplacement par Boolean et Double (permet d'accepter l'absence de ces champs dans le JSON)
    private Boolean locked;
    private Boolean enabled;
    private Double fidelityScore;
    private String fidelityLevel;
}
