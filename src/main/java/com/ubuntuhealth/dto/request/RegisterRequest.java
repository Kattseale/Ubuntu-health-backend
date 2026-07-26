package com.ubuntuhealth.dto.request;

import com.ubuntuhealth.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "First name can only contain letters and spaces."
    )
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Last name can only contain letters and spaces."
    )
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$",
            message = "Password must be 8-50 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character."
    )
    private String password;

    @NotNull(message = "Role is required.")
    private Role role;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must contain between 10 and 15 digits."
    )
    private String phoneNumber;
}