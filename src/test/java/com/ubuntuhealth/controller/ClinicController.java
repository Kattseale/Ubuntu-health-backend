package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.ClinicRequest;
import com.ubuntuhealth.dto.response.ClinicResponse;
import com.ubuntuhealth.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @PostMapping
    public ResponseEntity<ClinicResponse> createClinic(@RequestBody ClinicRequest request) {

        ClinicResponse response = clinicService.createClinic(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClinicResponse>> getAllClinics() {

        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinicById(@PathVariable Long id) {

        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicResponse> updateClinic(
            @PathVariable Long id,
            @RequestBody ClinicRequest request) {

        return ResponseEntity.ok(clinicService.updateClinic(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClinic(@PathVariable Long id) {

        clinicService.deleteClinic(id);

        return ResponseEntity.ok("Clinic deleted successfully.");
    }

}