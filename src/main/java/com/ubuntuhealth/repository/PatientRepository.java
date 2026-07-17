package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByClinicId(Long clinicId);

}