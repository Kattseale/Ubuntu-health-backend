package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    List<CommunityPost> findByClinicId(Long clinicId);

    List<CommunityPost> findByPatientId(Long patientId);

}