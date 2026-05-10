package com.sponsorbridge.sponsorbridge.dto;

import lombok.Data;

@Data
public class BrandProfileResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String companyName;
    private String industry;
    private String description;
    private String website;
    private Double totalSpent;
    private Integer totalDealsCreated;
}