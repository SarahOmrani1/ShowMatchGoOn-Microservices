package com.exemple.forumpromotion.dto;

import lombok.Data;

@Data
public class NotificationResponseDTO {
    private String id;
    private String title;
    private String message;
    private String type;
    private String userId;
}
