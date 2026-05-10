package com.sponsorbridge.sponsorbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sponsorbridge.sponsorbridge.enums.Niche;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "influencer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfluencerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne                           // one user has one influencer profile
    @JoinColumn(name = "user_id",       // creates user_id column in this table
            nullable = false,
            unique = true)          // one user can't have two profiles
    private User user;

    @Enumerated(EnumType.STRING)
    private Platform platform;          // YOUTUBE, INSTAGRAM, BOTH

    @Enumerated(EnumType.STRING)
    private Niche niche;                // TECH, FASHION etc.

    private Long followerCount;         // total followers

    private Double pricePerPost;        // how much they charge per post

    @Column(length = 500)
    private String bio;                 // short description

    private String channelOrHandle;     // YouTube channel name or Instagram handle

    private Double averageRating;       // calculated from reviews, default 0.0

    private Integer totalDealsCompleted; // track record
}
