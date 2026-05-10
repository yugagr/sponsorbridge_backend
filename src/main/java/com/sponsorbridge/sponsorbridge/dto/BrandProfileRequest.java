package com.sponsorbridge.sponsorbridge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BrandProfileRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String industry;
    private String description;
    private String website;
}