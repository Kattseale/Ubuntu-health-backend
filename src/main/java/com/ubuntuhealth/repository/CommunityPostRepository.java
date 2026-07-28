package com.ubuntuhealth.repository;

import com.ubuntuhealth.entity.CommunityPost;
import com.ubuntuhealth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {


    // Get posts belonging to a logged-in user
    List<CommunityPost> findByUser(User user);


    Collection<Object> findByUserId(Long userId);
}