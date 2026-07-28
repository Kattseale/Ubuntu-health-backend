package com.ubuntuhealth.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityPostResponse {

    private Long id;

    private Long userId;

    private String userName;

    private String userEmail;

    private String title;

    private String content;

    private Integer likes;

    private Integer comments;

    private LocalDateTime createdAt;

}