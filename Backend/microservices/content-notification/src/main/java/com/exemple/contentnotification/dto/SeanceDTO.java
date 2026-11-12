package com.exemple.contentnotification.dto;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SeanceDTO {
    private String id;
    private LocalDate dateSeance;
    private String heureSeance;
    private String salleId;
    private String cinemaId;
    private String contenuId;
}