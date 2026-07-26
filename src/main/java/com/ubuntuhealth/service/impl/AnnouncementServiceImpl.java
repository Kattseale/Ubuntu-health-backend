package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.AnnouncementRequest;
import com.ubuntuhealth.dto.response.AnnouncementResponse;
import com.ubuntuhealth.entity.Announcement;
import com.ubuntuhealth.entity.User;
import com.ubuntuhealth.exception.ResourceNotFoundException;
import com.ubuntuhealth.repository.AnnouncementRepository;
import com.ubuntuhealth.repository.UserRepository;
import com.ubuntuhealth.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    @Override
    public AnnouncementResponse createAnnouncement(AnnouncementRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .createdBy(user.getFirstName() + " " + user.getLastName())
                .createdByRole(user.getRole())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();

        announcementRepository.save(announcement);

        return mapToResponse(announcement);
    }

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {

        return announcementRepository
                .findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AnnouncementResponse updateAnnouncement(
            Long id,
            AnnouncementRequest request
    ) {

        Announcement announcement =
                announcementRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"));

        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setPriority(request.getPriority());

        announcementRepository.save(announcement);

        return mapToResponse(announcement);
    }

    @Override
    public void deleteAnnouncement(Long id) {

        Announcement announcement =
                announcementRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Announcement not found"));

        announcement.setActive(false);

        announcementRepository.save(announcement);
    }

    private AnnouncementResponse mapToResponse(
            Announcement announcement
    ) {

        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .description(announcement.getDescription())
                .priority(announcement.getPriority())
                .createdBy(announcement.getCreatedBy())
                .createdByRole(announcement.getCreatedByRole())
                .createdAt(announcement.getCreatedAt())
                .active(announcement.isActive())
                .build();
    }
}