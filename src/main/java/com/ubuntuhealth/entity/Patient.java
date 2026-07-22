package com.ubuntuhealth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String address;

    private String bloodGroup;

    @Column(nullable = false)
    private String emergencyContactName;

    @Column(nullable = false)
    private String emergencyContactPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;
}