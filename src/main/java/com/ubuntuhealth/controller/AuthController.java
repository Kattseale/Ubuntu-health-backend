package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.AuthResponse;
import com.ubuntuhealth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(
                new ApiResponse<>(true, "User registered successfully", response),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "User logged in successfully", response)
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.processForgotPassword(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Password reset instructions have been processed", null)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Password has been reset successfully", null)
        );
    }
}