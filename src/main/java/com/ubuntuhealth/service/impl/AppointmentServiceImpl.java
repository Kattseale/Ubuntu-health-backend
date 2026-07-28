package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.AppointmentRequest;
import com.ubuntuhealth.dto.response.AppointmentResponse;
import com.ubuntuhealth.entity.Appointment;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.Patient;
import com.ubuntuhealth.repository.AppointmentRepository;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.PatientRepository;
import com.ubuntuhealth.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Patient patient = patientRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Patient profile not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() ->
                        new RuntimeException("Clinic not found."));

        Appointment appointment = Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .patient(patient)
                .clinic(clinic)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(savedAppointment);
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found."));

        return mapToResponse(appointment);
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() ->
                        new RuntimeException("Clinic not found."));

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setClinic(clinic);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return mapToResponse(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found."));

        appointmentRepository.delete(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByClinic(Long clinicId) {

        return appointmentRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {

        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDate(LocalDate date) {

        return appointmentRepository.findByAppointmentDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ==========================
    // Helper Method
    // ==========================

    private AppointmentResponse mapToResponse(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .patientId(appointment.getPatient().getId())
                .patientName(
                        appointment.getPatient().getFirstName() + " " +
                                appointment.getPatient().getLastName()
                )
                .clinicId(appointment.getClinic().getId())
                .clinicName(appointment.getClinic().getClinicName())
                .build();
    }
}