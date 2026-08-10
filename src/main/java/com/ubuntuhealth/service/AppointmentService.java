package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.AppointmentRequest;
import com.ubuntuhealth.dto.response.AppointmentResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    // =========================================================
    // GET APPOINTMENTS BY CLINIC
    // =========================================================

    List<AppointmentResponse> getAppointmentsByClinic(
            Long clinicId
    );


    // =========================================================
    // GET APPOINTMENTS BY CLINIC AND DATE
    // =========================================================

    List<AppointmentResponse> getAppointmentsByClinicAndDate(
            Long clinicId,
            LocalDate date
    );


    // =========================================================
    // GET APPOINTMENTS BY PATIENT
    // =========================================================

    List<AppointmentResponse> getAppointmentsByPatient(
            Long patientId
    );


    // =========================================================
    // GET CURRENT PATIENT'S APPOINTMENTS
    // =========================================================

    List<AppointmentResponse> getMyAppointments();


    // =========================================================
    // GET APPOINTMENTS BY DATE
    // =========================================================

    List<AppointmentResponse> getAppointmentsByDate(
            LocalDate date
    );


    // =========================================================
    // CREATE APPOINTMENT
    // =========================================================

    AppointmentResponse createAppointment(
            AppointmentRequest request
    );


    // =========================================================
    // GET ALL APPOINTMENTS
    // =========================================================

    List<AppointmentResponse> getAllAppointments();


    // =========================================================
    // GET APPOINTMENT BY ID
    // =========================================================

    AppointmentResponse getAppointmentById(
            Long id
    );


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================

    AppointmentResponse updateAppointment(
            Long id,
            AppointmentRequest request
    );


    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    void deleteAppointment(Long id);


    // =========================================================
    // COMPLETE APPOINTMENT
    // =========================================================

    AppointmentResponse completeAppointment(Long id);
}