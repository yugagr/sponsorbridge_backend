package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.dto.DashboardResponse;
import com.sponsorbridge.sponsorbridge.dto.DealResponse;
import com.sponsorbridge.sponsorbridge.dto.ReviewResponse;
import com.sponsorbridge.sponsorbridge.entity.Deal;
import com.sponsorbridge.sponsorbridge.entity.Review;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.enums.Role;
import com.sponsorbridge.sponsorbridge.repository.DealRepository;
import com.sponsorbridge.sponsorbridge.repository.PaymentRepository;
import com.sponsorbridge.sponsorbridge.repository.ReviewRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DealRepository dealRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final AuthHelper authHelper;

    public DashboardResponse getDashboard() {

        User currentUser = authHelper.getCurrentUser();
        boolean isInfluencer = currentUser.getRole() == Role.INFLUENCER;

        // fetch all deals based on role
        List<Deal> allDeals = isInfluencer
                ? dealRepository.findByInfluencerId(currentUser.getId())
                : dealRepository.findByBrandId(currentUser.getId());

        // count deals by status
        long activeDeals = countByStatus(allDeals, DealStatus.ACTIVE);
        long completedDeals = countByStatus(allDeals, DealStatus.COMPLETED);
        long pendingDeals = countByStatus(allDeals, DealStatus.PENDING);
        long rejectedDeals = countByStatus(allDeals, DealStatus.REJECTED);

        // get last 5 deals for recent activity
        List<DealResponse> recentDeals = allDeals.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(this::mapDealToResponse)
                .toList();

        // get last 5 reviews received
        List<ReviewResponse> recentReviews = reviewRepository
                .findByRevieweeId(currentUser.getId())
                .stream()
                .limit(5)
                .map(this::mapReviewToResponse)
                .toList();

        // average rating
        Double avgRating = reviewRepository
                .getAverageRatingByUser(currentUser.getId());
        double roundedRating = avgRating != null
                ? Math.round(avgRating * 10.0) / 10.0
                : 0.0;

        // financial data based on role
        Double totalEarnings = null;
        Double totalSpent = null;

        if (isInfluencer) {
            Double earnings = paymentRepository
                    .getTotalEarningsByInfluencer(currentUser.getId());
            totalEarnings = earnings != null ? earnings : 0.0;
        } else {
            Double spent = paymentRepository
                    .getTotalSpentByBrand(currentUser.getId());
            totalSpent = spent != null ? spent : 0.0;
        }

        return DashboardResponse.builder()
                .name(currentUser.getName())
                .email(currentUser.getEmail())
                .role(currentUser.getRole().name())
                .averageRating(roundedRating)
                .totalDeals(allDeals.size())
                .activeDeals(activeDeals)
                .completedDeals(completedDeals)
                .pendingDeals(pendingDeals)
                .rejectedDeals(rejectedDeals)
                .totalEarnings(totalEarnings)
                .totalSpent(totalSpent)
                .recentDeals(recentDeals)
                .recentReviews(recentReviews)
                .build();
    }

    // ── private helpers ──────────────────────────────────────

    private long countByStatus(List<Deal> deals, DealStatus status) {
        return deals.stream()
                .filter(d -> d.getStatus() == status)
                .count();
    }

    private DealResponse mapDealToResponse(Deal deal) {
        DealResponse response = new DealResponse();
        response.setId(deal.getId());
        response.setTitle(deal.getTitle());
        response.setDescription(deal.getDescription());
        response.setBudgetAmount(deal.getBudgetAmount());
        response.setPlatform(deal.getPlatform());
        response.setStatus(deal.getStatus());
        response.setDeadline(deal.getDeadline());
        response.setCreatedAt(deal.getCreatedAt());
        response.setBrandId(deal.getBrand().getId());
        response.setBrandName(deal.getBrand().getName());
        response.setInfluencerId(deal.getInfluencer().getId());
        response.setInfluencerName(deal.getInfluencer().getName());
        return response;
    }

    private ReviewResponse mapReviewToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setReviewerName(review.getReviewer().getName());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}