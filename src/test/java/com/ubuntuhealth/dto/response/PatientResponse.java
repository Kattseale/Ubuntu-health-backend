package com.ubuntuhealth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientResponse {

    private Long id;

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

    private String clinicName;

}