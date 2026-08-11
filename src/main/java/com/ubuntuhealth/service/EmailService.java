package com.ubuntuhealth.service;

public interface EmailService {

    void sendVerificationEmail(
            String recipientEmail,
            String firstName,
            String verificationToken
    );
}