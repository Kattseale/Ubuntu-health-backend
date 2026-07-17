package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.MedicationRequest;
import com.ubuntuhealth.dto.response.MedicationResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.Medication;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.MedicationRepository;
import com.ubuntuhealth.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.cert.CertPathBuilder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public MedicationResponse createMedication(MedicationRequest request) {

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        Medication medication = Medication.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dosage(request.getDosage())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .available(request.getQuantity() > 0)
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
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        return mapToResponse(medication);
    }

    @Override
    public MedicationResponse updateMedication(Long id, MedicationRequest request) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        medication.setName(request.getName());
        medication.setDescription(request.getDescription());
        medication.setDosage(request.getDosage());
        medication.setQuantity(request.getQuantity());
        medication.setPrice(request.getPrice());
        medication.setAvailable(request.getQuantity() > 0);
        medication.setClinic(clinic);

        return mapToResponse(medicationRepository.save(medication));
    }

    @Override
    public void deleteMedication(Long id) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

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
    public MedicationResponse updateStock(Long id, Integer quantity) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        medication.setQuantity(quantity);
        medication.setAvailable(quantity > 0);

        return mapToResponse(medicationRepository.save(medication));
    }

    @Override
    public List<MedicationResponse> getAvailableMedications() {
        return List.of();
    }

    private MedicationResponse mapToResponse(Medication medication) {

        return MedicationResponse.builder()
                .id(medication.getId())
                .name(medication.getName())
                .description(medication.getDescription())
                .dosage(medication.getDosage())
                .quantity(medication.getQuantity())
                .price(medication.getPrice())
                .available(medication.getAvailable())
                .clinicId(medication.getClinic().getId())
                .clinicName(medication.getClinic().getClinicName())
                .build();
    }

    private CertPathBuilder clinicName(String clinicName) {
        return null;
    }
}