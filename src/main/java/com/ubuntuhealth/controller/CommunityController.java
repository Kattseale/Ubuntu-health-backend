package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.CommunityPostRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.CommunityPostResponse;
import com.ubuntuhealth.service.CommunityPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityPostService communityPostService;

    // Create a community post
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public CommunityPostResponse createPost(
            @Valid @RequestBody CommunityPostRequest request) {

        return communityPostService.createPost(request);
    }

    // Get all community posts
    @GetMapping
    public List<CommunityPostResponse> getAllPosts() {

        return communityPostService.getAllPosts();
    }

    // Get a community post by ID
    @GetMapping("/{id}")
    public CommunityPostResponse getPostById(@PathVariable Long id) {

        return communityPostService.getPostById(id);
    }

    // Update a community post
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public CommunityPostResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CommunityPostRequest request) {

        return communityPostService.updatePost(id, request);
    }

    // Delete a community post (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse deletePost(@PathVariable Long id) {

        return communityPostService.deletePost(id);
    }

    // Get posts by clinic
    @GetMapping("/clinic/{clinicId}")
    public List<CommunityPostResponse> getPostsByClinic(
            @PathVariable Long clinicId) {

        return communityPostService.getPostsByClinic(clinicId);
    }

    // Get posts by patient
    @GetMapping("/patient/{patientId}")
    public List<CommunityPostResponse> getPostsByPatient(
            @PathVariable Long patientId) {

        return communityPostService.getPostsByPatient(patientId);
    }
}