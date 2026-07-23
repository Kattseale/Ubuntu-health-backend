package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClinicRequest {

    @NotBlank(message = "Clinic name is required.")
    private String clinicName;

    @NotBlank(message = "Province is required.")
    private String province;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotNull(message = "Latitude is required.")
    private Double latitude;

    @NotNull(message = "Longitude is required.")
    private Double longitude;

    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @Email(message = "Please enter a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;
}