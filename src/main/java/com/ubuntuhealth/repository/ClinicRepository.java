package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    List<Clinic> findByCity(String city);

    List<Clinic> findByProvince(String province);

    List<Clinic> findByClinicNameContainingIgnoreCase(String clinicName);

    boolean existsByEmail(String email);

}