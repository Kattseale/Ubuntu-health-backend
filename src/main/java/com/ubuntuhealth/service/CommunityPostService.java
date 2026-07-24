package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.CommunityPostRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.CommunityPostResponse;

import java.util.List;

public interface CommunityPostService {

    CommunityPostResponse createPost(CommunityPostRequest request);

    List<CommunityPostResponse> getAllPosts();

    CommunityPostResponse getPostById(Long id);

    CommunityPostResponse updatePost(Long id, CommunityPostRequest request);

    ApiResponse deletePost(Long id);

    List<CommunityPostResponse> getPostsByClinic(Long clinicId);

    List<CommunityPostResponse> getPostsByPatient(Long patientId);

}