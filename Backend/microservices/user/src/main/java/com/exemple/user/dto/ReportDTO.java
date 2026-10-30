package com.exemple.user.dto;


import lombok.Data;

@Data
public class ReportDTO {
    private Long id;
    private Long reportedUserId;
    private String reason;
    private String status;
}
