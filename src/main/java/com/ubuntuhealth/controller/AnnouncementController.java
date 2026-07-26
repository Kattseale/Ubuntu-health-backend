package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.AnnouncementRequest;
import com.ubuntuhealth.dto.response.AnnouncementResponse;
import com.ubuntuhealth.service.AnnouncementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * Create Announcement
     * ADMIN, DOCTOR and NURSE
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(
            @Valid @RequestBody AnnouncementRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(announcementService.createAnnouncement(request));
    }

    /**
     * View all announcements
     * Everyone logged in
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements() {

        return ResponseEntity.ok(
                announcementService.getAllAnnouncements()
        );
    }

    /**
     * Update Announcement
     * ADMIN only
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request
    ) {

        return ResponseEntity.ok(
                announcementService.updateAnnouncement(id, request)
        );
    }

    /**
     * Delete Announcement
     * ADMIN only
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAnnouncement(
            @PathVariable Long id
    ) {

        announcementService.deleteAnnouncement(id);

        return ResponseEntity.ok("Announcement deleted successfully.");
    }
}