package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.MedicationRequest;
import com.ubuntuhealth.dto.response.MedicationResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.Medication;
import com.ubuntuhealth.exception.ResourceNotFoundException;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.MedicationRepository;
import com.ubuntuhealth.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public MedicationResponse createMedication(MedicationRequest request) {

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found."));

        Medication medication = Medication.builder()
                .medicationName(request.getMedicationName())
                .description(request.getDescription())
                .dosage(request.getDosage())
                .quantityAvailable(request.getQuantityAvailable())
                .available(request.getQuantityAvailable() > 0)
                .clinic(clinic)
                .build();

        return mapToResponse(medicationRepository.save(medication));
    }

    @Override
    public List<MedicationResponse> getAllMedications() {
        return medicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MedicationResponse getMedicationById(Long id) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found."));

        return mapToResponse(medication);
    }

    @Override
    public MedicationResponse updateMedication(Long id, MedicationRequest request) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found."));

        medication.setMedicationName(request.getMedicationName());
        medication.setDescription(request.getDescription());
        medication.setDosage(request.getDosage());
        medication.setQuantityAvailable(request.getQuantityAvailable());
        medication.setAvailable(request.getQuantityAvailable() > 0);
        medication.setClinic(clinic);

        return mapToResponse(medicationRepository.save(medication));
    }

    @Override
    public void deleteMedication(Long id) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found."));

        medicationRepository.delete(medication);
    }

    @Override
    public List<MedicationResponse> getMedicationsByClinic(Long clinicId) {

        return medicationRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MedicationResponse updateStock(Long id, Integer quantityAvailable) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found."));

        medication.setQuantityAvailable(quantityAvailable);
        medication.setAvailable(quantityAvailable > 0);

        return mapToResponse(medicationRepository.save(medication));
    }

    @Override
    public List<MedicationResponse> getAvailableMedications() {

        return medicationRepository.findByAvailableTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MedicationResponse mapToResponse(Medication medication) {

        return MedicationResponse.builder()
                .id(medication.getId())
                .medicationName(medication.getMedicationName())
                .description(medication.getDescription())
                .dosage(medication.getDosage())
                .quantityAvailable(medication.getQuantityAvailable())
                .available(medication.getAvailable())
                .clinicId(medication.getClinic().getId())
                .clinicName(medication.getClinic().getClinicName())
                .build();
    }
}