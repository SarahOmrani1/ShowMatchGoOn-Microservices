package com.exemple.contentnotification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Sanction renvoyée par le microservice moderation (FastAPI renvoie les champs en snake_case)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SanctionDTO {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("sanction_type")
    private String sanctionType;

    private String reason;

    @JsonProperty("duration_days")
    private Integer durationDays;

    @JsonProperty("applied_at")
    private String appliedAt;
}
