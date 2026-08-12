package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.ChangePasswordRequest;
import com.ubuntuhealth.dto.request.ForgotPasswordRequest;
import com.ubuntuhealth.dto.request.LoginRequest;
import com.ubuntuhealth.dto.request.RegisterRequest;
import com.ubuntuhealth.dto.request.ResendVerificationRequest;
import com.ubuntuhealth.dto.request.ResetPasswordRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.LoginResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.EmailVerificationToken;
import com.ubuntuhealth.entity.PasswordResetToken;
import com.ubuntuhealth.entity.Patient;
import com.ubuntuhealth.entity.Role;
import com.ubuntuhealth.entity.User;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.EmailVerificationTokenRepository;
import com.ubuntuhealth.repository.PasswordResetTokenRepository;
import com.ubuntuhealth.repository.PatientRepository;
import com.ubuntuhealth.repository.UserRepository;
import com.ubuntuhealth.security.JwtService;
import com.ubuntuhealth.service.AuthService;
import com.ubuntuhealth.service.EmailService;

import jakarta.transaction.Transactional;

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

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    private final EmailService emailService;


    // ============================================================
    // REGISTER
    // ============================================================

    @Override
    @Transactional
    public ApiResponse register(RegisterRequest request) {

        // ========================================================
        // CHECK EMAIL
        // ========================================================

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists."
            );
        }


        // ========================================================
        // CHECK PHONE NUMBER
        // ========================================================

        if (userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            throw new RuntimeException(
                    "Phone number already exists."
            );
        }


        // ========================================================
        // CREATE USER
        // ========================================================

        User user = User.builder()

                .firstName(request.getFirstName())

                .lastName(request.getLastName())

                .email(request.getEmail())

                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )

                .phoneNumber(request.getPhoneNumber())

                .role(request.getRole())

                .enabled(true)

                .emailVerified(false)

                .passwordChanged(false)

                .failedLoginAttempts(0)

                .accountLocked(false)

                .build();


        User savedUser = userRepository.save(user);


        // ========================================================
        // CREATE PATIENT PROFILE
        // ========================================================

        if (savedUser.getRole() == Role.PATIENT) {

            Clinic clinic = clinicRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "No clinics exist. Please create a clinic first."
                            )
                    );


            Patient patient = Patient.builder()

                    .firstName(
                            savedUser.getFirstName()
                    )

                    .lastName(
                            savedUser.getLastName()
                    )

                    .email(
                            savedUser.getEmail()
                    )

                    .phoneNumber(
                            savedUser.getPhoneNumber()
                    )

                    .clinic(clinic)

                    .user(savedUser)

                    .gender("")

                    .address("")

                    .bloodGroup("")

                    .dateOfBirth(null)

                    .emergencyContactName(
                            "Not Provided"
                    )

                    .emergencyContactPhone(
                            "Not Provided"
                    )

                    .build();


            patientRepository.save(patient);
        }


        // ========================================================
        // DELETE OLD VERIFICATION TOKEN
        // ========================================================

        emailVerificationTokenRepository
                .findByUserId(savedUser.getId())
                .ifPresent(
                        emailVerificationTokenRepository::delete
                );


        // ========================================================
        // GENERATE VERIFICATION TOKEN
        // ========================================================

        String verificationToken =
                UUID.randomUUID().toString();


        // ========================================================
        // CREATE VERIFICATION TOKEN
        // ========================================================

        EmailVerificationToken emailToken =
                EmailVerificationToken.builder()

                        .token(verificationToken)

                        .user(savedUser)

                        .expiryDate(
                                LocalDateTime.now()
                                        .plusMinutes(30)
                        )

                        .build();


        // ========================================================
        // SAVE TOKEN
        // ========================================================

        emailVerificationTokenRepository.save(
                emailToken
        );


        // ========================================================
        // SEND EMAIL
        // ========================================================

        emailService.sendVerificationEmail(
                savedUser.getEmail(),
                savedUser.getFirstName(),
                verificationToken
        );


        // ========================================================
        // SUCCESS
        // ========================================================

        return new ApiResponse(
                "Registration successful. " +
                        "Please check your email and click the verification link " +
                        "to activate your account."
        );
    }


    // ============================================================
    // RESEND VERIFICATION EMAIL
    // ============================================================

    @Override
    @Transactional
    public ApiResponse resendVerificationEmail(
            ResendVerificationRequest request
    ) {

        // ========================================================
        // CLEAN EMAIL
        // ========================================================

        String email = request.getEmail()
                .trim()
                .toLowerCase();


        // ========================================================
        // FIND USER
        // ========================================================

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No account was found with this email address."
                        )
                );


        // ========================================================
        // CHECK IF ALREADY VERIFIED
        // ========================================================

        if (Boolean.TRUE.equals(
                user.getEmailVerified()
        )) {

            return new ApiResponse(
                    "This email address has already been verified. " +
                            "You can log in."
            );
        }


        // ========================================================
        // CHECK ACCOUNT ENABLED
        // ========================================================

        if (!Boolean.TRUE.equals(
                user.getEnabled()
        )) {

            throw new RuntimeException(
                    "This account has been disabled. " +
                            "Please contact the administrator."
            );
        }


        // ========================================================
        // DELETE OLD TOKEN
        // ========================================================

        emailVerificationTokenRepository
                .findByUserId(user.getId())
                .ifPresent(
                        emailVerificationTokenRepository::delete
                );


        // ========================================================
        // GENERATE NEW TOKEN
        // ========================================================

        String verificationToken =
                UUID.randomUUID().toString();


        // ========================================================
        // CREATE NEW TOKEN
        // ========================================================

        EmailVerificationToken emailToken =
                EmailVerificationToken.builder()

                        .token(verificationToken)

                        .user(user)

                        .expiryDate(
                                LocalDateTime.now()
                                        .plusMinutes(30)
                        )

                        .build();


        // ========================================================
        // SAVE NEW TOKEN
        // ========================================================

        emailVerificationTokenRepository.save(
                emailToken
        );


        // ========================================================
        // SEND VERIFICATION EMAIL
        // ========================================================

        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getFirstName(),
                verificationToken
        );


        // ========================================================
        // SUCCESS
        // ========================================================

        return new ApiResponse(
                "A new verification email has been sent to " +
                        user.getEmail() +
                        ". Please check your inbox."
        );
    }


    // ============================================================
    // VERIFY EMAIL
    // ============================================================

    @Override
    @Transactional
    public ApiResponse verifyEmail(String token) {

        // ========================================================
        // CHECK TOKEN
        // ========================================================

        if (token == null ||
                token.trim().isEmpty()) {

            throw new RuntimeException(
                    "Verification token is required."
            );
        }


        // ========================================================
        // FIND TOKEN
        // ========================================================

        EmailVerificationToken verificationToken =
                emailVerificationTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid verification token."
                                )
                        );


        // ========================================================
        // CHECK EXPIRATION
        // ========================================================

        if (verificationToken
                .getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            emailVerificationTokenRepository
                    .delete(verificationToken);

            throw new RuntimeException(
                    "Verification token has expired. " +
                            "Please request a new verification email."
            );
        }


        // ========================================================
        // GET USER
        // ========================================================

        User user =
                verificationToken.getUser();


        // ========================================================
        // CHECK IF ALREADY VERIFIED
        // ========================================================

        if (Boolean.TRUE.equals(
                user.getEmailVerified()
        )) {

            emailVerificationTokenRepository
                    .delete(verificationToken);

            return new ApiResponse(
                    "Email address has already been verified."
            );
        }


        // ========================================================
        // VERIFY EMAIL
        // ========================================================

        user.setEmailVerified(true);

        user.setEnabled(true);

        userRepository.save(user);


        // ========================================================
        // DELETE USED TOKEN
        // ========================================================

        emailVerificationTokenRepository
                .delete(verificationToken);


        // ========================================================
        // SUCCESS
        // ========================================================

        return new ApiResponse(
                "Email verified successfully. " +
                        "Your account is now active."
        );
    }


    // ============================================================
    // LOGIN
    // ============================================================

    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password."
                        )
                );


        // ========================================================
        // CHECK ACCOUNT ENABLED
        // ========================================================

        if (!Boolean.TRUE.equals(
                user.getEnabled()
        )) {

            throw new RuntimeException(
                    "Your account has been disabled. " +
                            "Please contact the administrator."
            );
        }


        // ========================================================
        // CHECK EMAIL VERIFICATION
        // ========================================================

        if (!Boolean.TRUE.equals(
                user.getEmailVerified()
        )) {

            throw new RuntimeException(
                    "Please verify your email address " +
                            "before logging in."
            );
        }


        // ========================================================
        // CHECK ACCOUNT LOCK
        // ========================================================

        if (Boolean.TRUE.equals(
                user.getAccountLocked()
        )) {

            if (user.getLockTime() != null &&
                    user.getLockTime()
                            .plusMinutes(30)
                            .isBefore(
                                    LocalDateTime.now()
                            )) {

                user.setAccountLocked(false);

                user.setFailedLoginAttempts(0);

                user.setLockTime(null);

                userRepository.save(user);

            } else {

                throw new RuntimeException(
                        "Your account has been locked due to " +
                                "multiple failed login attempts. " +
                                "Please try again after 30 minutes."
                );
            }
        }


        // ========================================================
        // AUTHENTICATE USER
        // ========================================================

        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception ex) {

            int attempts =
                    user.getFailedLoginAttempts() + 1;


            user.setFailedLoginAttempts(
                    attempts
            );


            // ====================================================
            // LOCK AFTER 5 ATTEMPTS
            // ====================================================

            if (attempts >= 5) {

                user.setAccountLocked(true);

                user.setLockTime(
                        LocalDateTime.now()
                );

                userRepository.save(user);

                throw new RuntimeException(
                        "Your account has been locked after " +
                                "5 failed login attempts. " +
                                "Please try again after 30 minutes."
                );
            }


            userRepository.save(user);


            throw new RuntimeException(
                    "Invalid email or password. " +
                            "Remaining attempts: " +
                            (5 - attempts)
            );
        }


        // ========================================================
        // LOGIN SUCCESSFUL
        // ========================================================

        user.setFailedLoginAttempts(0);

        user.setAccountLocked(false);

        user.setLockTime(null);

        userRepository.save(user);


        // ========================================================
        // GENERATE JWT
        // ========================================================

        String token =
                jwtService.generateToken(user);


        return LoginResponse.builder()

                .token(token)

                .email(user.getEmail())

                .role(user.getRole())

                .message("Login successful.")

                .build();
    }


    // ============================================================
// FORGOT PASSWORD
// ============================================================

    @Override
    public ApiResponse forgotPassword(
            ForgotPasswordRequest request) {

        // ========================================================
        // FIND USER
        // ========================================================

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found."
                        )
                );


        // ========================================================
        // CHECK ACCOUNT ENABLED
        // ========================================================

        if (!Boolean.TRUE.equals(
                user.getEnabled())) {

            throw new RuntimeException(
                    "This account has been disabled."
            );
        }


        // ========================================================
        // DELETE OLD RESET TOKEN
        // ========================================================

        passwordResetTokenRepository
                .findByUserId(user.getId())
                .ifPresent(
                        passwordResetTokenRepository::delete
                );


        // ========================================================
        // GENERATE RESET TOKEN
        // ========================================================

        String token =
                UUID.randomUUID().toString();


        // ========================================================
        // CREATE RESET TOKEN
        // ========================================================

        PasswordResetToken resetToken =
                PasswordResetToken.builder()

                        .token(token)

                        .user(user)

                        .expiryDate(
                                LocalDateTime.now()
                                        .plusMinutes(30)
                        )

                        .build();


        // ========================================================
        // SAVE RESET TOKEN
        // ========================================================

        passwordResetTokenRepository.save(
                resetToken
        );


        // ========================================================
        // SEND RESET EMAIL
        // ========================================================

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getFirstName(),
                token
        );


        // ========================================================
        // SUCCESS
        // ========================================================

        return new ApiResponse(
                "Password reset instructions have been sent "
                        + "to your email address."
        );
    }

    @Override
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        return null;
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest request) {
        return null;
    }
}