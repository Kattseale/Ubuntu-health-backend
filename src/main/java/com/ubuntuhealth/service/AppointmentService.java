package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.AppointmentRequest;
import com.ubuntuhealth.dto.response.AppointmentResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAllAppointments();

    AppointmentResponse getAppointmentById(Long id);

    AppointmentResponse updateAppointment(Long id, AppointmentRequest request);

    void deleteAppointment(Long id);

    List<AppointmentResponse> getAppointmentsByClinic(Long clinicId);

    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);

    List<AppointmentResponse> getAppointmentsByDate(LocalDate date);
}