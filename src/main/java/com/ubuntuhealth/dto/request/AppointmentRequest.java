package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {

    @NotNull(message = "Appointment date is required.")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required.")
    private LocalTime appointmentTime;

    @NotBlank(message = "Reason is required.")
    private String reason;

    @NotBlank(message = "Status is required.")
    private String status;

    @NotNull(message = "Patient is required.")
    private Long patientId;

    @NotNull(message = "Clinic is required.")
    private Long clinicId;
}