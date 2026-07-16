package com.ubuntuhealth.repository;

import com.ubuntuhealth.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // Custom query: Fetches all announcements and displays the newest ones first
    List<Announcement> findAllByOrderByCreatedAtDesc();
}