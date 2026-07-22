package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.PatientRequest;
import com.ubuntuhealth.dto.response.PatientResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.Patient;
import com.ubuntuhealth.exception.DuplicateResourceException;
import com.ubuntuhealth.exception.ResourceNotFoundException;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.PatientRepository;
import com.ubuntuhealth.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public PatientResponse createPatient(PatientRequest request) {

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found."));
        
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Patient email already exists.");
        }

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .bloodGroup(request.getBloodGroup())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .clinic(clinic)
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found."));

        return mapToResponse(patient);
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setGender(request.getGender());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setAddress(request.getAddress());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setClinic(clinic);

        Patient updatedPatient = patientRepository.save(patient);

        return mapToResponse(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patientRepository.delete(patient);
    }

    @Override
    public List<PatientResponse> getPatientsByClinic(Long clinicId) {
        return patientRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .clinicId(patient.getClinic().getId())
                .clinicName(patient.getClinic().getClinicName())
                .build();
    }
}