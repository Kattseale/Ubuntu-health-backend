package com.ubuntuhealth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnnouncementResponse {

    private Long id;

    private String title;

    private String message;

    private LocalDateTime createdAt;

    private String createdBy;
}