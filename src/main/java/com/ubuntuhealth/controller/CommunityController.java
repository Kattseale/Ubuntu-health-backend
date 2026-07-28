package com.ubuntuhealth.controller;

import com.ubuntuhealth.dto.request.CommunityPostRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.CommunityPostResponse;
import com.ubuntuhealth.service.CommunityPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommunityController {

    private final CommunityPostService communityPostService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {

        e.printStackTrace();

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }
    // ================= CREATE =================

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public CommunityPostResponse createPost(
            @Valid @RequestBody CommunityPostRequest request) {

        return communityPostService.createPost(request);
    }


    // ================= READ =================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT','DOCTOR','RECEPTIONIST')")
    public List<CommunityPostResponse> getAllPosts() {

        return communityPostService.getAllPosts();
    }


    // ================= USER POSTS =================

    @GetMapping("/my-posts")
    @PreAuthorize("hasRole('PATIENT')")
    public List<CommunityPostResponse> getMyPosts() {

        return communityPostService.getMyPosts();
    }


    // ================= UPDATE =================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT')")
    public CommunityPostResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CommunityPostRequest request) {

        return communityPostService.updatePost(id, request);
    }


    // ================= DELETE =================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    public ApiResponse deletePost(
            @PathVariable Long id) {

        return communityPostService.deletePost(id);
    }
}