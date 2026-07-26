package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // Fetch all announcements newest first
    List<Announcement> findAllByOrderByCreatedAtDesc();

    // Fetch announcements by author role (e.g., ADMIN only or PATIENT only)
    List<Announcement> findByAuthorRoleOrderByCreatedAtDesc(String authorRole);
}