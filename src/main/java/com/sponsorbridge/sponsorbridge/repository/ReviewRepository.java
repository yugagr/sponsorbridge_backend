package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // all reviews written about a user
    List<Review> findByRevieweeId(Long revieweeId);

    // all reviews written by a user
    List<Review> findByReviewerId(Long reviewerId);

    // average rating for a user
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewee.id = :userId")
    Double getAverageRatingByUser(@Param("userId") Long userId);

    // check if reviewer already reviewed this deal
    boolean existsByDealIdAndReviewerId(Long dealId, Long reviewerId);
}
