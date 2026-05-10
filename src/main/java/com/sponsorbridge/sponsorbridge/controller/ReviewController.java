package com.sponsorbridge.sponsorbridge.controller;

import com.sponsorbridge.sponsorbridge.dto.ReviewRequest;
import com.sponsorbridge.sponsorbridge.entity.Review;
import com.sponsorbridge.sponsorbridge.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // POST /api/reviews
    @PostMapping
    public ResponseEntity<Review> createReview(
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    // GET /api/reviews/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsForUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsForUser(userId));
    }

    // GET /api/reviews/user/{userId}/rating
    @GetMapping("/user/{userId}/rating")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getAverageRating(userId));
    }
}