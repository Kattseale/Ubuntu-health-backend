package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.AppointmentRequest;
import com.ubuntuhealth.dto.response.AppointmentResponse;
import com.ubuntuhealth.entity.Appointment;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.Patient;
import com.ubuntuhealth.exception.ResourceNotFoundException;
import com.ubuntuhealth.repository.AppointmentRepository;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.PatientRepository;
import com.ubuntuhealth.service.AppointmentService;
import lombok.RequiredArgsConstructor;
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

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found."));

        Appointment appointment = Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .status(request.getStatus())
                .patient(patient)
                .clinic(clinic)
                .build();

        return mapToResponse(appointmentRepository.save(appointment));
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
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found."));

        return mapToResponse(appointment);
    }

    @Override
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found."));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found."));

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(request.getStatus());
        appointment.setPatient(patient);
        appointment.setClinic(clinic);

        return mapToResponse(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found."));

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