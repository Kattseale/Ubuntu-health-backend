package com.ubuntuhealth.controller;

import com.ubuntuhealth.entity.Announcement;
import com.ubuntuhealth.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    // View all announcements (Admin and Patient posts combined)
    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    // Filter by role if needed: /api/announcements/role/ADMIN or /api/announcements/role/PATIENT
    @GetMapping("/role/{role}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByRole(@PathVariable String role) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByRole(role.toUpperCase()));
    }

    // Create a new announcement (Both Admin and Patient can post)
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        Announcement created = announcementService.createAnnouncement(announcement);
        return ResponseEntity.ok(created);
    }

    // Delete an announcement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
}