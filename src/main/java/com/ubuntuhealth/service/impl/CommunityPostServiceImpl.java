package com.ubuntuhealth.service.impl;

import com.ubuntuhealth.dto.request.CommunityPostRequest;
import com.ubuntuhealth.dto.response.ApiResponse;
import com.ubuntuhealth.dto.response.CommunityPostResponse;
import com.ubuntuhealth.entity.CommunityPost;
import com.ubuntuhealth.entity.User;
import com.ubuntuhealth.repository.CommunityPostRepository;
import com.ubuntuhealth.repository.UserRepository;
import com.ubuntuhealth.service.CommunityPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityPostServiceImpl implements CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final UserRepository userRepository;

    @Override
    public CommunityPostResponse createPost(CommunityPostRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        CommunityPost post = CommunityPost.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .authorName(user.getFirstName() + " " + user.getLastName())
                .authorEmail(user.getEmail())
                .build();

        CommunityPost savedPost = communityPostRepository.save(post);

        return mapToResponse(savedPost);
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
                .orElseThrow(() ->
                        new RuntimeException("Community post not found."));

        return mapToResponse(post);
    }

    @Override
    public CommunityPostResponse updatePost(
            Long id,
            CommunityPostRequest request) {

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Community post not found."));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        CommunityPost updatedPost = communityPostRepository.save(post);

        return mapToResponse(updatedPost);
    }

    @Override
    public ApiResponse deletePost(Long id) {

        CommunityPost post = communityPostRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Community post not found."));

        communityPostRepository.delete(post);

        return new ApiResponse("Community post deleted successfully.");
    }

    @Override
    public List<CommunityPostResponse> getPostsByUser(Long userId) {

        return communityPostRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CommunityPostResponse mapToResponse(CommunityPost post) {

        return CommunityPostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .userName(post.getAuthorName())
                .userEmail(post.getAuthorEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .likes(post.getLikes())
                .comments(post.getComments())
                .createdAt(post.getCreatedAt())
                .build();
    }
}