package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.ChangePasswordRequest;
import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.LoginResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.PasswordResetToken;
import com.ubuntuhealth.entity.Patient;
import com.ubuntuhealth.entity.Role;
import com.ubuntuhealth.entity.User;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.PasswordResetTokenRepository;
import com.ubuntuhealth.repository.PatientRepository;
import com.ubuntuhealth.repository.UserRepository;
import com.ubuntuhealth.security.JwtService;
import com.ubuntuhealth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public ApiResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .enabled(true)
                .passwordChanged(false)
                .build();

        User savedUser = userRepository.save(user);

        // Automatically create Patient profile
        if (savedUser.getRole() == Role.PATIENT) {

            Clinic clinic = clinicRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException("No clinics exist. Please create a clinic first."));

            Patient patient = Patient.builder()
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .clinic(clinic)
                    .user(savedUser)

                    // Temporary default values
                    .gender("")
                    .address("")
                    .bloodGroup("")
                    .dateOfBirth(null)
                    .emergencyContactName("Not Provided")
                    .emergencyContactPhone("Not Provided")
                    .build();

            patientRepository.save(patient);
        }

        return new ApiResponse("User registered successfully.");
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password."));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException(
                    "Your account has been disabled. Please contact the administrator."
            );
        }

        if (Boolean.TRUE.equals(user.getAccountLocked())) {

            if (user.getLockTime() != null &&
                    user.getLockTime().plusMinutes(30).isBefore(LocalDateTime.now())) {

                user.setAccountLocked(false);
                user.setFailedLoginAttempts(0);
                user.setLockTime(null);

                userRepository.save(user);

            } else {

                throw new RuntimeException(
                        "Your account has been locked due to multiple failed login attempts. Please try again after 30 minutes."
                );
            }
        }

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception ex) {

            int attempts = user.getFailedLoginAttempts() + 1;

            user.setFailedLoginAttempts(attempts);

            if (attempts >= 5) {

                user.setAccountLocked(true);
                user.setLockTime(LocalDateTime.now());

                userRepository.save(user);

                throw new RuntimeException(
                        "Your account has been locked after 5 failed login attempts. Please try again after 30 minutes."
                );
            }

            userRepository.save(user);

            throw new RuntimeException(
                    "Invalid email or password. Remaining attempts: " + (5 - attempts)
            );
        }

        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .message("Login successful.")
                .build();
    }

    @Override
    public ApiResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        if (!user.getEnabled()) {
            throw new RuntimeException("This account has been disabled.");
        }

        passwordResetTokenRepository.findByUserId(user.getId())
                .ifPresent(passwordResetTokenRepository::delete);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();

        passwordResetTokenRepository.save(resetToken);

        return new ApiResponse(
                "Password reset token generated successfully: " + token
        );
    }

    @Override
    public ApiResponse resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() ->
                        new RuntimeException("Invalid reset token."));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            passwordResetTokenRepository.delete(resetToken);

            throw new RuntimeException("Reset token has expired.");
        }

        User user = resetToken.getUser();

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the current password.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChanged(true);

        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

        return new ApiResponse("Password reset successfully.");
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found."));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new RuntimeException("Current password is incorrect.");
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "New password cannot be the same as the current password."
            );
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChanged(true);

        userRepository.save(user);

        return new ApiResponse("Password changed successfully.");
    }
}