package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.dto.ReviewRequest;
import com.sponsorbridge.sponsorbridge.entity.Deal;
import com.sponsorbridge.sponsorbridge.entity.InfluencerProfile;
import com.sponsorbridge.sponsorbridge.entity.Review;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.repository.DealRepository;
import com.sponsorbridge.sponsorbridge.repository.InfluencerRepository;
import com.sponsorbridge.sponsorbridge.repository.ReviewRepository;
import com.sponsorbridge.sponsorbridge.repository.UserRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final InfluencerRepository influencerRepository;
    private final AuthHelper authHelper;

    @Transactional
    public Review createReview(ReviewRequest request) {

        User reviewer = authHelper.getCurrentUser();

        // deal must exist
        Deal deal = dealRepository.findById(request.getDealId())
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        // deal must be completed before reviewing
        if (deal.getStatus() != DealStatus.COMPLETED) {
            throw new RuntimeException(
                    "Reviews can only be left after deal is completed");
        }

        // reviewer must be part of this deal
        boolean isBrand = deal.getBrand().getId().equals(reviewer.getId());
        boolean isInfluencer = deal.getInfluencer().getId().equals(reviewer.getId());

        if (!isBrand && !isInfluencer) {
            throw new RuntimeException("You are not part of this deal");
        }

        // cannot review twice for same deal
        if (reviewRepository.existsByDealIdAndReviewerId(
                request.getDealId(), reviewer.getId())) {
            throw new RuntimeException("You have already reviewed this deal");
        }

        // validate rating
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        User reviewee = userRepository.findById(request.getRevieweeId())
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        Review review = Review.builder()
                .deal(deal)
                .reviewer(reviewer)
                .reviewee(reviewee)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review saved = reviewRepository.save(review);

        // update influencer's average rating automatically
        updateInfluencerRating(reviewee.getId());

        return saved;
    }

    // get all reviews about a user
    public List<Review> getReviewsForUser(Long userId) {
        return reviewRepository.findByRevieweeId(userId);
    }

    // get average rating for a user
    public Double getAverageRating(Long userId) {
        Double avg = reviewRepository.getAverageRatingByUser(userId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
        // rounds to 1 decimal place e.g. 4.3
    }

    // ── private helper ───────────────────────────────────────

    // recalculates and updates influencer's averageRating field
    private void updateInfluencerRating(Long userId) {
        influencerRepository.findByUserId(userId).ifPresent(profile -> {
            Double avg = reviewRepository.getAverageRatingByUser(userId);
            if (avg != null) {
                // round to 1 decimal
                profile.setAverageRating(Math.round(avg * 10.0) / 10.0);
                influencerRepository.save(profile);
            }
        });
    }
}