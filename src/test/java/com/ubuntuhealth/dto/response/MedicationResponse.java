package com.ubuntuhealth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationResponse {

    private Long id;

    private String name;

    private String description;

    private String dosage;

    private Integer quantity;

    private Double price;

    private Boolean available;

    private Long clinicId;

    private String clinicName;
}