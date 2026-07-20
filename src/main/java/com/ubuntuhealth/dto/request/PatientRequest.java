package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

        @NotBlank(message = "First name is required.")
        private String firstName;

        @NotBlank(message = "Last name is required.")
        private String lastName;

        @NotBlank(message = "Gender is required.")
        private String gender;

        @NotNull(message = "Date of birth is required.")
        private LocalDate dateOfBirth;

        @Email(message = "Please enter a valid email address.")
        @NotBlank(message = "Email is required.")
        private String email;

        @NotBlank(message = "Phone number is required.")
        private String phoneNumber;

        @NotBlank(message = "Address is required.")
        private String address;

        @NotBlank(message = "Blood group is required.")
        private String bloodGroup;

        @NotBlank(message = "Emergency contact is required.")
        private String emergencyContact;

        @NotNull(message = "Clinic ID is required.")
        private Long clinicId;
    }
