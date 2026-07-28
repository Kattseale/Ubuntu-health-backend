package com.ubuntuhealth.dto.response;

import com.ubuntuhealth.entity.Priority;
import com.ubuntuhealth.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnnouncementResponse {

    private Long id;

    private String title;

    private String description;

    private Priority priority;

    private String createdBy;

    private Role createdByRole;

    private LocalDateTime createdAt;

    private boolean active;

}