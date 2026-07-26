package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.AnnouncementRequest;
import com.ubuntuhealth.dto.response.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {

    AnnouncementResponse create(AnnouncementRequest request);

    List<AnnouncementResponse> getAll();

    void delete(Long id);
}