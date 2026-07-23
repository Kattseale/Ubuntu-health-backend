package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.MedicationRequest;
import com.ubuntuhealth.dto.response.MedicationResponse;

import java.util.List;

public interface MedicationService {

    MedicationResponse createMedication(MedicationRequest request);

    List<MedicationResponse> getAllMedications();

    MedicationResponse getMedicationById(Long id);

    MedicationResponse updateMedication(Long id, MedicationRequest request);

    void deleteMedication(Long id);

    List<MedicationResponse> getMedicationsByClinic(Long clinicId);

    MedicationResponse updateStock(Long id, Integer quantity);

    List<MedicationResponse> getAvailableMedications();

    class AppointmentService {
    }
}