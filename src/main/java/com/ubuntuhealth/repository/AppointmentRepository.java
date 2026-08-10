package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {


    // =========================================================
    // BY CLINIC
    // =========================================================

    List<Appointment> findByClinicId(Long clinicId);


    // =========================================================
    // BY PATIENT
    // =========================================================

    List<Appointment> findByPatientId(Long patientId);


    // =========================================================
    // BY DATE
    // =========================================================

    List<Appointment> findByAppointmentDate(
            LocalDate appointmentDate
    );


    // =========================================================
    // BY CLINIC + DATE
    // =========================================================

    List<Appointment> findByClinicIdAndAppointmentDate(
            Long clinicId,
            LocalDate appointmentDate
    );


    // =========================================================
    // CHECK WHETHER CONFIRMED SLOT EXISTS
    // =========================================================

    boolean existsByClinicIdAndAppointmentDateAndAppointmentTimeAndStatus(
            Long clinicId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            String status
    );


    // =========================================================
    // GET APPOINTMENTS BY CLINIC + DATE + STATUS
    // =========================================================

    List<Appointment> findByClinicIdAndAppointmentDateAndStatus(
            Long clinicId,
            LocalDate appointmentDate,
            String status
    );
}