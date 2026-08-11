package com.ubuntuhealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===============================
    // Personal Information
    // ===============================

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    // ===============================
    // Login Information
    // ===============================

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    // ===============================
    // Role
    // ===============================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ===============================
    // Account Status
    // ===============================

    /**
     * New accounts are disabled until
     * the user verifies their email.
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean enabled = false;

    // ===============================
    // Email Verification
    // ===============================

    /**
     * Indicates whether the user's
     * email address has been verified.
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean emailVerified = false;

    // ===============================
    // Password Status
    // ===============================

    @Builder.Default
    @Column(nullable = false)
    private Boolean passwordChanged = false;

    // ===============================
    // Account Lock Security
    // ===============================

    @Builder.Default
    @Column(nullable = false)
    private Integer failedLoginAttempts = 0;

    @Builder.Default
    @Column(nullable = false)
    private Boolean accountLocked = false;

    @Column
    private LocalDateTime lockTime;
}