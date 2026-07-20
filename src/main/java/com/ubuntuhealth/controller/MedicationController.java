package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.MedicationRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.MedicationResponse;
import com.ubuntuhealth.service.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(
            @Valid @RequestBody MedicationRequest request) {

        MedicationResponse response = medicationService.createMedication(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getAllMedications() {

        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponse> getMedicationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponse> updateMedication(
            @PathVariable Long id,
            @Valid @RequestBody MedicationRequest request) {

        return ResponseEntity.ok(
                medicationService.updateMedication(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMedication(
            @PathVariable Long id) {

        medicationService.deleteMedication(id);

        return ResponseEntity.ok(
                new ApiResponse("Medication deleted successfully.")
        );
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<MedicationResponse>> getMedicationsByClinic(
            @PathVariable Long clinicId) {

        return ResponseEntity.ok(
                medicationService.getMedicationsByClinic(clinicId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<MedicationResponse>> getAvailableMedications() {

        return ResponseEntity.ok(
                medicationService.getAvailableMedications());
    }
}