package com.sponsorbridge.sponsorbridge.entity;

import com.sponsorbridge.sponsorbridge.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data                   // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // generates empty constructor
@AllArgsConstructor     // generates constructor with all fields
@Builder                // lets you create objects like User.builder().name("x").build()
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;            // we will store hashed password later

    @Enumerated(EnumType.STRING)        // saves "INFLUENCER" or "BRAND" in DB, not 0 or 1
    @Column(nullable = false)
    private Role role;

    private String profilePicture;      // file path or URL

    @Column(updatable = false)          // set once on creation, never updated
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}