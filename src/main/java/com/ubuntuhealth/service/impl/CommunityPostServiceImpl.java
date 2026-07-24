package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.CommunityPostRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.CommunityPostResponse;
import com.ubuntuhealth.entity.Clinic;
import com.ubuntuhealth.entity.CommunityPost;
import com.ubuntuhealth.entity.Patient;
import com.ubuntuhealth.repository.ClinicRepository;
import com.ubuntuhealth.repository.CommunityPostRepository;
import com.ubuntuhealth.repository.PatientRepository;
import com.ubuntuhealth.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPostServiceImpl implements CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public CommunityPostResponse createPost(CommunityPostRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found."));

        CommunityPost post = CommunityPost.builder()
                .message(request.getMessage())
                .patient(patient)
                .clinic(clinic)
                .build();

        communityPostRepository.save(post);

        return mapToResponse(post);
    }

    @Override
    public List<CommunityPostResponse> getAllPosts() {

        return communityPostRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CommunityPostResponse getPostById(Long id) {

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community post not found."));

        return mapToResponse(post);
    }

    @Override
    public CommunityPostResponse updatePost(Long id, CommunityPostRequest request) {

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community post not found."));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found."));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clinic not found."));

        post.setMessage(request.getMessage());
        post.setPatient(patient);
        post.setClinic(clinic);

        communityPostRepository.save(post);

        return mapToResponse(post);
    }

    @Override
    public ApiResponse deletePost(Long id) {

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Community post not found."));

        communityPostRepository.delete(post);

        return new ApiResponse("Community post deleted successfully.");
    }

    @Override
    public List<CommunityPostResponse> getPostsByClinic(Long clinicId) {

        return communityPostRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CommunityPostResponse> getPostsByPatient(Long patientId) {

        return communityPostRepository.findByPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CommunityPostResponse mapToResponse(CommunityPost post) {

        return CommunityPostResponse.builder()
                .id(post.getId())
                .message(post.getMessage())
                .createdAt(post.getCreatedAt())
                .patientId(post.getPatient().getId())
                .patientName(post.getPatient().getFirstName() + " " + post.getPatient().getLastName())
                .clinicId(post.getClinic().getId())
                .clinicName(post.getClinic().getClinicName())
                .build();
    }
}