package com.ubuntuhealth.service;

import com.ubuntuhealth.entity.Announcement;
import com.ubuntuhealth.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Announcement> getAnnouncementsByRole(String role) {
        return announcementRepository.findByAuthorRoleOrderByCreatedAtDesc(role);
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}