package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {

    // Common Fields
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phoneNumber;
    private String gender;

    // Patient Specific Fields
    private String idNumber;
    private String address;

    // Doctor Specific Fields
    private String specialty;
    private String licenseNumber;

    // Doctor & Admin Common Fields
    private String clinicName;

    // Admin Specific Fields
    private String employeeNumber;

    // Role
    private Set<String> roles;
}