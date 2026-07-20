package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByClinicId(Long clinicId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

}