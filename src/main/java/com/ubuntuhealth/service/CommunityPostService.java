package com.ubuntuhealth.service;

import com.ubuntuhealth.dto.request.CommunityPostRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.CommunityPostResponse;

import java.util.List;

public interface CommunityPostService {


    /**
     * Create a new community post
     * using the logged-in patient.
     */
    CommunityPostResponse createPost(
            CommunityPostRequest request
    );


    /**
     * Get all community posts.
     */
    List<CommunityPostResponse> getAllPosts();


    /**
     * Get a community post by ID.
     */
    CommunityPostResponse getPostById(Long id);


    /**
     * Update an existing community post.
     */
    CommunityPostResponse updatePost(
            Long id,
            CommunityPostRequest request
    );


    /**
     * Delete a community post.
     */
    ApiResponse deletePost(Long id);


    /**
     * Get posts belonging to the logged-in patient.
     */
    List<CommunityPostResponse> getMyPosts();

    List<CommunityPostResponse> getPostsByUser(Long userId);
}