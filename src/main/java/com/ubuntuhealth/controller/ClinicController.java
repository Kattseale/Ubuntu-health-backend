package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.ClinicRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.ClinicResponse;
import com.ubuntuhealth.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;


    // =========================================================
    // CREATE CLINIC
    // ADMIN ONLY
    // =========================================================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClinicResponse> createClinic(
            @Valid @RequestBody ClinicRequest request) {

        ClinicResponse response =
                clinicService.createClinic(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET ALL CLINICS
    // PATIENT + ADMIN
    // =========================================================

    @GetMapping
    public ResponseEntity<List<ClinicResponse>> getAllClinics() {

        return ResponseEntity.ok(
                clinicService.getAllClinics()
        );
    }


    // =========================================================
    // GET CLINIC BY ID
    // PATIENT + ADMIN
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinicById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                clinicService.getClinicById(id)
        );
    }


    // =========================================================
    // UPDATE CLINIC
    // ADMIN ONLY
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClinicResponse> updateClinic(
            @PathVariable Long id,
            @Valid @RequestBody ClinicRequest request) {

        return ResponseEntity.ok(
                clinicService.updateClinic(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // DELETE CLINIC
    // ADMIN ONLY
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteClinic(
            @PathVariable Long id) {

        clinicService.deleteClinic(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Clinic deleted successfully."
                )
        );
    }
}