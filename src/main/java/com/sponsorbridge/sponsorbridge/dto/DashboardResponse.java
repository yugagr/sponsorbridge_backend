package com.sponsorbridge.sponsorbridge.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardResponse {

    // ── common fields (both influencer and brand) ──
    private String name;
    private String email;
    private String role;
    private Double averageRating;

    // ── deal counts ──
    private long totalDeals;
    private long activeDeals;
    private long completedDeals;
    private long pendingDeals;
    private long rejectedDeals;

    // ── financial ──
    private Double totalEarnings;       // influencer only
    private Double totalSpent;          // brand only

    // ── recent deals (last 5) ──
    private List<DealResponse> recentDeals;

    // ── reviews received ──
    private List<ReviewResponse> recentReviews;
}