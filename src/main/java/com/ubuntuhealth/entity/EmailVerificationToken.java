package com.ubuntuhealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===============================
    // Verification Token
    // ===============================

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    // ===============================
    // User
    // ===============================

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    // ===============================
    // Expiration
    // ===============================

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    // ===============================
    // Verification Status
    // ===============================

    @Builder.Default
    @Column(nullable = false)
    private Boolean used = false;
}