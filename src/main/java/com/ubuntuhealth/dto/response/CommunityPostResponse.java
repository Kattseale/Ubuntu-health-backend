package com.ubuntuhealth.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostResponse {

    private Long id;

    private String message;

    private LocalDateTime createdAt;

    private Long patientId;

    private String patientName;

    private Long clinicId;

    private String clinicName;

}