package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.AnnouncementRequest;
import com.ubuntuhealth.dto.response.AnnouncementResponse;
import com.ubuntuhealth.entity.Announcement;
import com.ubuntuhealth.repository.AnnouncementRepository;
import com.ubuntuhealth.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository repository;

    @Override
    public AnnouncementResponse create(AnnouncementRequest request) {

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .createdBy("Admin")
                .build();

        repository.save(announcement);

        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .message(announcement.getMessage())
                .createdAt(announcement.getCreatedAt())
                .createdBy(announcement.getCreatedBy())
                .build();
    }

    @Override
    public List<AnnouncementResponse> getAll() {

        return repository.findAll().stream().map(a ->
                AnnouncementResponse.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .message(a.getMessage())
                        .createdAt(a.getCreatedAt())
                        .createdBy(a.getCreatedBy())
                        .build()
        ).toList();
    }

    @Override
    public void delete(Long id) {

        repository.deleteById(id);

    }
}