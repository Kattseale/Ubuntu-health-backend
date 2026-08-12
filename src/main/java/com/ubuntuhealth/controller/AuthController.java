package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.ChangePasswordRequest;
import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResendVerificationRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.LoginResponse;
import com.ubuntuhealth.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5174")
public class AuthController {

    private final AuthService authService;


    // ============================================================
    // REGISTER
    // ============================================================

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return ResponseEntity.ok(
                authService.register(request)
        );
    }


    // ============================================================
    // VERIFY EMAIL
    // ============================================================

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(
            @RequestParam String token
    ) {

        return ResponseEntity.ok(
                authService.verifyEmail(token)
        );
    }


    // ============================================================
    // RESEND VERIFICATION EMAIL
    // ============================================================

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse> resendVerificationEmail(
            @Valid @RequestBody ResendVerificationRequest request
    ) {

        return ResponseEntity.ok(
                authService.resendVerificationEmail(request)
        );
    }


    // ============================================================
    // LOGIN
    // ============================================================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }


    // ============================================================
    // FORGOT PASSWORD
    // ============================================================

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        return ResponseEntity.ok(
                authService.forgotPassword(request)
        );
    }


    // ============================================================
    // RESET PASSWORD
    // ============================================================

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        return ResponseEntity.ok(
                authService.resetPassword(request)
        );
    }


    // ============================================================
    // CHANGE PASSWORD
    // ============================================================

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        return ResponseEntity.ok(
                authService.changePassword(request)
        );
    }
}