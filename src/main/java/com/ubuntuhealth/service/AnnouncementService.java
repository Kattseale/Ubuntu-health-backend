package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.AnnouncementRequest;
import com.ubuntuhealth.dto.response.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {

    AnnouncementResponse createAnnouncement(AnnouncementRequest request);

    List<AnnouncementResponse> getAllAnnouncements();

    AnnouncementResponse updateAnnouncement(Long id, AnnouncementRequest request);

    void deleteAnnouncement(Long id);

}