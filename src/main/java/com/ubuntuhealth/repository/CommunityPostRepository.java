package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.CommunityPost;
import com.ubuntuhealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    List<CommunityPost> findByUser(User user);

    List<CommunityPost> findByUserId(Long userId);

}