package com.ubuntuhealth.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private String email;

    private String phoneNumber;

    private String address;

    private String bloodGroup;

    private String emergencyContact;

    private Long clinicId;

}