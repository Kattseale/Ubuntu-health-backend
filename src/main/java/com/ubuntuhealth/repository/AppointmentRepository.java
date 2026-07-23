package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByClinicId(Long clinicId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
}