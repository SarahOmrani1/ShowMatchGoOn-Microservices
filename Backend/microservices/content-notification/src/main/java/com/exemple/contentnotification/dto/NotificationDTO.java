package com.exemple.contentnotification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;

    @NotBlank(message = "Message is mandatory")
    private String message;

    private String title;

    @NotBlank(message = "Type is mandatory")
    @Pattern(regexp = "INFO|SUCCESS|WARNING|ERROR", message = "Type must be INFO, SUCCESS, WARNING, or ERROR")
    private String type;

    private LocalDateTime createdAt;

    @Builder.Default
    private Boolean isRead = false;

    @NotBlank(message = "UserId is mandatory")
    private String userId;

    private String username;

    @Email(message = "Email must be a valid address")
    private String email;
}
