package com.sponsorbridge.sponsorbridge.dto;

import com.sponsorbridge.sponsorbridge.enums.Niche;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import lombok.Data;

@Data
public class InfluencerProfileResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private Platform platform;
    private Niche niche;
    private Long followerCount;
    private Double pricePerPost;
    private String bio;
    private String channelOrHandle;
    private Double averageRating;
    private Integer totalDealsCompleted;
}