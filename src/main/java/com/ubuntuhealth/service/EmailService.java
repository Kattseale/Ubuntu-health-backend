package com.ubuntuhealth.service;

public interface EmailService {

    // ============================================================
    // SEND VERIFICATION EMAIL
    // ============================================================

    void sendVerificationEmail(
            String recipientEmail,
            String firstName,
            String verificationToken
    );


    // ============================================================
    // SEND PASSWORD RESET EMAIL
    // ============================================================

    void sendPasswordResetEmail(
            String recipientEmail,
            String firstName,
            String resetToken
    );
}