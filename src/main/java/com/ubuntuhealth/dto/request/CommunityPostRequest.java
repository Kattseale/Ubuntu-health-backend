package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostRequest {

    @NotBlank(message = "Message is required.")
    private String message;

    @NotNull(message = "Patient ID is required.")
    private Long patientId;

    @NotNull(message = "Clinic ID is required.")
    private Long clinicId;

}