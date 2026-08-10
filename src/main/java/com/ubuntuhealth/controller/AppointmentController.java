package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.AppointmentRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.AppointmentResponse;
import com.ubuntuhealth.service.impl.AppointmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServiceImpl appointmentService;


    // =========================================================
    // CREATE APPOINTMENT
    // =========================================================

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        AppointmentResponse response =
                appointmentService.createAppointment(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET APPOINTMENTS BY CLINIC + DATE
    // =========================================================

    @GetMapping("/clinic/{clinicId}/date/{date}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>>
    getAppointmentsByClinicAndDate(
            @PathVariable Long clinicId,
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                appointmentService
                        .getAppointmentsByClinicAndDate(
                                clinicId,
                                date
                        )
        );
    }


    // =========================================================
    // GET ALL APPOINTMENTS
    // =========================================================

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppointmentResponse>>
    getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }


    // =========================================================
    // GET MY APPOINTMENTS
    // =========================================================

    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponse>>
    getMyAppointments() {

        return ResponseEntity.ok(
                appointmentService.getMyAppointments()
        );
    }


    // =========================================================
    // GET APPOINTMENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse>
    getAppointmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponse>
    updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequest request) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse>
    deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Appointment cancelled successfully."
                )
        );
    }


    // =========================================================
    // COMPLETE APPOINTMENT
    // =========================================================

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppointmentResponse>
    completeAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.completeAppointment(id)
        );
    }


    // =========================================================
    // GET APPOINTMENTS BY CLINIC
    // =========================================================

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>>
    getAppointmentsByClinic(
            @PathVariable Long clinicId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByClinic(
                        clinicId
                )
        );
    }


    // =========================================================
    // GET APPOINTMENTS BY PATIENT
    // =========================================================

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>>
    getAppointmentsByPatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatient(
                        patientId
                )
        );
    }


    // =========================================================
    // GET APPOINTMENTS BY DATE
    // =========================================================

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponse>>
    getAppointmentsByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDate(date)
        );
    }
}

