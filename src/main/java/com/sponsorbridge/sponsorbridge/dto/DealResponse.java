package com.sponsorbridge.sponsorbridge.dto;

import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DealResponse {
    private Long id;
    private String title;
    private String description;
    private Double budgetAmount;
    private Platform platform;
    private DealStatus status;
    private LocalDate deadline;
    private String counterOffer;
    private LocalDateTime createdAt;

    // instead of full User objects, just names and ids
    private Long brandId;
    private String brandName;
    private Long influencerId;
    private String influencerName;
}