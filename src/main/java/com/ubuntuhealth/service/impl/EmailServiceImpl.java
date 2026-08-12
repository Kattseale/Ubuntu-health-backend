package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;


    // ============================================================
    // SEND VERIFICATION EMAIL
    // ============================================================

    @Override
    public void sendVerificationEmail(
            String recipientEmail,
            String firstName,
            String verificationToken
    ) {

        // ========================================================
        // CREATE VERIFICATION LINK
        // ========================================================

        String verificationLink =
                frontendUrl
                        + "/verify-email?token="
                        + verificationToken;


        // ========================================================
        // CREATE EMAIL
        // ========================================================

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);

        message.setTo(recipientEmail);

        message.setSubject(
                "Ubuntu Health - Verify Your Email"
        );

        message.setText(

                "Hello " + firstName + ",\n\n"

                        + "Welcome to Ubuntu Health!\n\n"

                        + "Thank you for creating your Ubuntu Health account.\n\n"

                        + "Please verify your email address by clicking "
                        + "the link below:\n\n"

                        + verificationLink + "\n\n"

                        + "This verification link will expire in 30 minutes.\n\n"

                        + "If you did not create an Ubuntu Health account, "
                        + "you can safely ignore this email.\n\n"

                        + "Regards,\n"
                        + "Ubuntu Health Team"
        );


        // ========================================================
        // SEND EMAIL
        // ========================================================

        mailSender.send(message);
    }


    // ============================================================
    // SEND PASSWORD RESET EMAIL
    // ============================================================

    @Override
    public void sendPasswordResetEmail(
            String recipientEmail,
            String firstName,
            String resetToken
    ) {

        // ========================================================
        // CREATE PASSWORD RESET LINK
        // ========================================================

        String resetLink =
                frontendUrl
                        + "/reset-password?token="
                        + resetToken;


        // ========================================================
        // CREATE EMAIL
        // ========================================================

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);

        message.setTo(recipientEmail);

        message.setSubject(
                "Ubuntu Health - Reset Your Password"
        );

        message.setText(

                "Hello " + firstName + ",\n\n"

                        + "We received a request to reset the password "
                        + "for your Ubuntu Health account.\n\n"

                        + "You can reset your password by clicking "
                        + "the link below:\n\n"

                        + resetLink + "\n\n"

                        + "This password reset link will expire in "
                        + "30 minutes.\n\n"

                        + "If you did not request a password reset, "
                        + "you can safely ignore this email. "
                        + "Your password will remain unchanged.\n\n"

                        + "Regards,\n"
                        + "Ubuntu Health Team"
        );


        // ========================================================
        // SEND EMAIL
        // ========================================================

        mailSender.send(message);
    }
}