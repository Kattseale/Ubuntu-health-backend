package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.ChangePasswordRequest;
import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.LoginResponse;
import com.ubuntuhealth.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;


    // ============================================================
    // REGISTER
    // ============================================================

    @PostMapping("/register")
    public ApiResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }


    // ============================================================
    // LOGIN
    // ============================================================

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }


    // ============================================================
    // VERIFY EMAIL
    // ============================================================

    @GetMapping("/verify-email")
    public ApiResponse verifyEmail(
            @RequestParam("token") String token
    ) {

        return authService.verifyEmail(token);
    }


    // ============================================================
    // FORGOT PASSWORD
    // ============================================================

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        return authService.forgotPassword(request);
    }


    // ============================================================
    // RESET PASSWORD
    // ============================================================

    @PostMapping("/reset-password")
    public ApiResponse resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        return authService.resetPassword(request);
    }


    // ============================================================
    // CHANGE PASSWORD
    // ============================================================

    @PostMapping("/change-password")
    public ApiResponse changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        return authService.changePassword(request);
    }
}