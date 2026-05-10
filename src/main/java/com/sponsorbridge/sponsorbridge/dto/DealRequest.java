package com.sponsorbridge.sponsorbridge.dto;

import com.sponsorbridge.sponsorbridge.enums.Platform;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DealRequest {

    @NotNull(message = "Influencer ID is required")
    private Long influencerId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Budget is required")
    private Double budgetAmount;

    private Platform platform;

    private LocalDate deadline;

    // brand can add deliverables when creating the deal
    private List<String> deliverableTitles;
}
