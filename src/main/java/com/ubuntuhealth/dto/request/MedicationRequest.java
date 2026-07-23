package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationRequest {

    @NotBlank(message = "Medication name is required.")
    private String medicationName;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotBlank(message = "Dosage is required.")
    private String dosage;

    @NotNull(message = "Quantity is required.")
    @Min(value = 0, message = "Quantity cannot be negative.")
    private Integer quantityAvailable;

    @NotNull(message = "Clinic ID is required.")
    private Long clinicId;
}