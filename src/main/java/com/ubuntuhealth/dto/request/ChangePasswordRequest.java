package com.ubuntuhealth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required.")
    private String currentPassword;

    @NotBlank(message = "New password is required.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$",
            message = "New password must be 8-50 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character."
    )
    private String newPassword;

    @NotBlank(message = "Please confirm your new password.")
    private String confirmPassword;

}