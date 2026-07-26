package com.ubuntuhealth.dto.request;

import com.ubuntuhealth.entity.Priority;
import lombok.Data;

@Data
public class AnnouncementRequest {

    private String title;

    private String description;

    private Priority priority;

}