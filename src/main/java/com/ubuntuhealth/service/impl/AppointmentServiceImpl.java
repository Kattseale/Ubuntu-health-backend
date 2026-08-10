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


    // =========================================================
    // GET APPOINTMENTS BY CLINIC AND DATE
    // =========================================================

    @Override
    public List<AppointmentResponse> getAppointmentsByClinicAndDate(
            Long clinicId,
            LocalDate date) {

        /*
         * ONLY CONFIRMED appointments block a slot.
         *
         * COMPLETED and CANCELLED appointments do not
         * block the slot.
         */

        return appointmentRepository
                .findByClinicIdAndAppointmentDateAndStatus(
                        clinicId,
                        date,
                        "CONFIRMED"
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // CREATE APPOINTMENT
    // =========================================================

    @Override
    public AppointmentResponse createAppointment(
            AppointmentRequest request) {

        /*
         * Check whether the requested slot is already confirmed.
         */

        boolean alreadyBooked =
                appointmentRepository
                        .existsByClinicIdAndAppointmentDateAndAppointmentTimeAndStatus(
                                request.getClinicId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                "CONFIRMED"
                        );

        if (alreadyBooked) {

            throw new RuntimeException(
                    "This appointment time has already been booked."
            );
        }


        // =====================================================
        // GET CURRENT USER
        // =====================================================

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "User authentication not found."
            );
        }

        String email = authentication.getName();


        // =====================================================
        // FIND PATIENT
        // =====================================================

        Patient patient =
                patientRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient profile not found."
                                )
                        );


        // =====================================================
        // FIND CLINIC
        // =====================================================

        Clinic clinic =
                clinicRepository
                        .findById(request.getClinicId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Clinic not found."
                                )
                        );


        // =====================================================
        // CREATE APPOINTMENT
        // =====================================================

        Appointment appointment =
                Appointment.builder()
                        .appointmentDate(
                                request.getAppointmentDate()
                        )
                        .appointmentTime(
                                request.getAppointmentTime()
                        )
                        .reason(
                                request.getReason()
                        )
                        .status("CONFIRMED")
                        .patient(patient)
                        .clinic(clinic)
                        .build();


        Appointment savedAppointment =
                appointmentRepository.save(appointment);


        return mapToResponse(savedAppointment);
    }


    // =========================================================
    // GET ALL APPOINTMENTS
    // =========================================================

    @Override
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET APPOINTMENT BY ID
    // =========================================================

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found."
                                )
                        );

        return mapToResponse(appointment);
    }


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================

    @Override
    public AppointmentResponse updateAppointment(
            Long id,
            AppointmentRequest request) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found."
                                )
                        );


        boolean sameSlot =
                appointment
                        .getClinic()
                        .getId()
                        .equals(request.getClinicId())

                        && appointment
                        .getAppointmentDate()
                        .equals(request.getAppointmentDate())

                        && appointment
                        .getAppointmentTime()
                        .equals(request.getAppointmentTime());


        boolean alreadyBooked =
                appointmentRepository
                        .existsByClinicIdAndAppointmentDateAndAppointmentTimeAndStatus(
                                request.getClinicId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                "CONFIRMED"
                        );


        if (alreadyBooked && !sameSlot) {

            throw new RuntimeException(
                    "This appointment time has already been booked."
            );
        }


        Clinic clinic =
                clinicRepository
                        .findById(request.getClinicId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Clinic not found."
                                )
                        );


        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setReason(
                request.getReason()
        );

        appointment.setClinic(clinic);

        /*
         * Updating an appointment makes it confirmed again.
         */
        appointment.setStatus("CONFIRMED");


        Appointment updatedAppointment =
                appointmentRepository.save(appointment);


        return mapToResponse(updatedAppointment);
    }


    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    @Override
    public void deleteAppointment(Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found."
                                )
                        );


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "User authentication not found."
            );
        }


        String email = authentication.getName();


        Patient patient =
                patientRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient profile not found."
                                )
                        );


        if (!appointment
                .getPatient()
                .getId()
                .equals(patient.getId())) {

            throw new RuntimeException(
                    "You are not allowed to cancel this appointment."
            );
        }


        if ("CANCELLED".equals(
                appointment.getStatus())) {

            throw new RuntimeException(
                    "This appointment has already been cancelled."
            );
        }


        appointment.setStatus("CANCELLED");

        appointmentRepository.save(appointment);
    }


    // =========================================================
    // COMPLETE APPOINTMENT
    // =========================================================

    @Override
    public AppointmentResponse completeAppointment(Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found."
                                )
                        );


        // =====================================================
        // CHECK CURRENT STATUS
        // =====================================================

        if ("CANCELLED".equalsIgnoreCase(
                appointment.getStatus())) {

            throw new RuntimeException(
                    "A cancelled appointment cannot be completed."
            );
        }


        if ("COMPLETED".equalsIgnoreCase(
                appointment.getStatus())) {

            throw new RuntimeException(
                    "This appointment is already completed."
            );
        }


        // =====================================================
        // MARK AS COMPLETED
        // =====================================================

        appointment.setStatus("COMPLETED");


        Appointment savedAppointment =
                appointmentRepository.save(appointment);


        return mapToResponse(savedAppointment);
    }


    // =========================================================
    // GET APPOINTMENTS BY CLINIC
    // =========================================================

    @Override
    public List<AppointmentResponse> getAppointmentsByClinic(
            Long clinicId) {

        return appointmentRepository
                .findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET APPOINTMENTS BY PATIENT
    // =========================================================

    @Override
    public List<AppointmentResponse> getAppointmentsByPatient(
            Long patientId) {

        return appointmentRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET CURRENT PATIENT'S APPOINTMENTS
    // =========================================================

    @Override
    public List<AppointmentResponse> getMyAppointments() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "User authentication not found."
            );
        }


        String email = authentication.getName();


        Patient patient =
                patientRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient profile not found."
                                )
                        );


        return appointmentRepository
                .findByPatientId(patient.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET APPOINTMENTS BY DATE
    // =========================================================

    @Override
    public List<AppointmentResponse> getAppointmentsByDate(
            LocalDate date) {

        return appointmentRepository
                .findByAppointmentDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // MAP APPOINTMENT TO RESPONSE
    // =========================================================

    private AppointmentResponse mapToResponse(
            Appointment appointment) {

        return AppointmentResponse
                .builder()

                .id(
                        appointment.getId()
                )

                .appointmentDate(
                        appointment.getAppointmentDate()
                )

                .appointmentTime(
                        appointment.getAppointmentTime()
                )

                .reason(
                        appointment.getReason()
                )

                .status(
                        appointment.getStatus()
                )

                .patientId(
                        appointment
                                .getPatient()
                                .getId()
                )

                .patientName(
                        appointment
                                .getPatient()
                                .getFirstName()
                                + " " +
                                appointment
                                        .getPatient()
                                        .getLastName()
                )

                .clinicId(
                        appointment
                                .getClinic()
                                .getId()
                )

                .clinicName(
                        appointment
                                .getClinic()
                                .getClinicName()
                )

                .build();
    }
}