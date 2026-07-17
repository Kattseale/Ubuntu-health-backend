package com.ubuntuhealth.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationRequest {

    private String name;

    private String description;

    private String dosage;

    private Integer quantity;

    private Double price;

    private Long clinicId;
}