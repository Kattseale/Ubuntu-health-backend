package com.ubuntuhealth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationResponse {

    private Long id;

    private String medicationName;

    private String description;

    private String dosage;

    private Integer quantityAvailable;

    private Boolean available;

    private Long clinicId;

    private String clinicName;
}