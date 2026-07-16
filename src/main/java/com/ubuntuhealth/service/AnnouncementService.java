package com.ubuntuhealth.service;

import com.ubuntuhealth.model.Announcement;
import com.ubuntuhealth.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    // Create and save a new clinic announcement
    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    // Get all announcements, newest first, to display on dashboards
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByCreatedAtDesc();
    }

    // Delete an announcement by ID
    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}