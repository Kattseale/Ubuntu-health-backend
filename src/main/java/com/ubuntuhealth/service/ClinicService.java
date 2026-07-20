package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.ClinicRequest;
import com.ubuntuhealth.dto.response.ClinicResponse;

import java.util.List;

public interface ClinicService {

    ClinicResponse createClinic(ClinicRequest request);

    ClinicResponse getClinicById(Long id);

    List<ClinicResponse> getAllClinics();

    ClinicResponse updateClinic(Long id, ClinicRequest request);

    void deleteClinic(Long id);

}