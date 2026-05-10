package com.sponsorbridge.sponsorbridge.dto;

import com.sponsorbridge.sponsorbridge.enums.Niche;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InfluencerProfileRequest {

    @NotNull(message = "Platform is required")
    private Platform platform;

    @NotNull(message = "Niche is required")
    private Niche niche;

    private Long followerCount;
    private Double pricePerPost;
    private String bio;
    private String channelOrHandle;
}
