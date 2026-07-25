package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void processForgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}