package com.ubuntuhealth.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The unique ID used for logging in (Patient ID or Employee Num)[cite: 1]
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role; // PATIENT, CLINIC_ADMIN, or MEDICAL_PRACTITIONER[cite: 1]

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "contact_number")
    private String contactNumber;

    // Specific field examples for different roles[cite: 1]
    @Column(name = "address")
    private String address; // For Patients[cite: 1]

    @Column(name = "clinic_name")
    private String clinicName; // For Admins and Doctors[cite: 1]

    @Column(name = "practising_license_number")
    private String practisingLicenseNumber; // For Doctors[cite: 1]

    @Column(name = "specialty")
    private String specialty; // For Doctors[cite: 1]
}