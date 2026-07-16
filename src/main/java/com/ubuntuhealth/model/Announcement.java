package com.ubuntuhealth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Note: This connects the announcement to the User (Admin) table
    // (This assumes Member 1 is creating the 'User' model in com.ubuntuhealth.model)
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    // Constructor (Empty)
    public Announcement() {}

    // Constructor (With Fields)
    public Announcement(String title, String content, User admin) {
        this.title = title;
        this.content = content;
        this.admin = admin;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getAdmin() { return admin; }
    public void setAdmin(User admin) { this.admin = admin; }
}