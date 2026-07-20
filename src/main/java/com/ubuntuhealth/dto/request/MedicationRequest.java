package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.DecimalMin;
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
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotBlank(message = "Dosage is required.")
    private String dosage;

    @NotNull(message = "Quantity is required.")
    @Min(value = 0, message = "Quantity cannot be negative.")
    private Integer quantity;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Price must be greater than 0.")
    private Double price;

    @NotNull(message = "Clinic ID is required.")
    private Long clinicId;
}