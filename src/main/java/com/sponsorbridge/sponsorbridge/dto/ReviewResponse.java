package com.sponsorbridge.sponsorbridge.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private String reviewerName;
    private LocalDateTime createdAt;
}