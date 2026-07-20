package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    List<Medication> findByClinicId(Long clinicId);
    List<Medication> findByAvailableTrue();

}