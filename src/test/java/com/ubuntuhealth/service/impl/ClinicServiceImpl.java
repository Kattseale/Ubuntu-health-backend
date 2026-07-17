package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.ClinicRequest;
import com.ubuntuhealth.dto.response.ClinicResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    @Override
    public ClinicResponse createClinic(ClinicRequest request) {

        if (clinicRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Clinic email already exists.");
        }

        Clinic clinic = Clinic.builder()
                .clinicName(request.getClinicName())
                .province(request.getProvince())
                .city(request.getCity())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();

        clinic = clinicRepository.save(clinic);

        return mapToResponse(clinic);
    }

    @Override
    public ClinicResponse getClinicById(Long id) {

        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found."));

        return mapToResponse(clinic);
    }

    @Override
    public List<ClinicResponse> getAllClinics() {

        return clinicRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClinicResponse updateClinic(Long id, ClinicRequest request) {

        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found."));

        clinic.setClinicName(request.getClinicName());
        clinic.setProvince(request.getProvince());
        clinic.setCity(request.getCity());
        clinic.setAddress(request.getAddress());
        clinic.setLatitude(request.getLatitude());
        clinic.setLongitude(request.getLongitude());
        clinic.setPhoneNumber(request.getPhoneNumber());
        clinic.setEmail(request.getEmail());

        clinic = clinicRepository.save(clinic);

        return mapToResponse(clinic);
    }

    @Override
    public void deleteClinic(Long id) {

        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found."));

        clinicRepository.delete(clinic);
    }

    private ClinicResponse mapToResponse(Clinic clinic) {

        return ClinicResponse.builder()
                .id(clinic.getId())
                .clinicName(clinic.getClinicName())
                .province(clinic.getProvince())
                .city(clinic.getCity())
                .address(clinic.getAddress())
                .latitude(clinic.getLatitude())
                .longitude(clinic.getLongitude())
                .phoneNumber(clinic.getPhoneNumber())
                .email(clinic.getEmail())
                .build();
    }
}