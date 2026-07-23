package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.PatientRequest;
import com.ubuntuhealth.dto.response.PatientResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

public interface PatientService {

    PatientResponse createPatient(PatientRequest request);

    List<PatientResponse> getAllPatients();

    PatientResponse getPatientById(Long id);

    PatientResponse updatePatient(Long id, PatientRequest request);

    void deletePatient(Long id);

    List<PatientResponse> getPatientsByClinic(Long clinicId);

}