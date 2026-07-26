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
@CrossOrigin(origins = "*")
public class CommunityController {

    private final CommunityPostService communityPostService;

    // ================= CREATE =================

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public CommunityPostResponse createPost(
            @Valid @RequestBody CommunityPostRequest request) {

        return communityPostService.createPost(request);
    }

    // ================= READ =================

    @GetMapping
    public List<CommunityPostResponse> getAllPosts() {

        return communityPostService.getAllPosts();
    }

    @GetMapping("/{id}")
    public CommunityPostResponse getPostById(
            @PathVariable Long id) {

        return communityPostService.getPostById(id);
    }

    @GetMapping("/user/{userId}")
    public List<CommunityPostResponse> getPostsByUser(
            @PathVariable Long userId) {

        return communityPostService.getPostsByUser(userId);
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public CommunityPostResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CommunityPostRequest request) {

        return communityPostService.updatePost(id, request);
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deletePost(
            @PathVariable Long id) {

        return communityPostService.deletePost(id);
    }
}