package com.sponsorbridge.sponsorbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;              // who wrote this review

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;              // who is being reviewed

    @Column(nullable = false)
    private Integer rating;             // 1 to 5

    @Column(length = 500)
    private String comment;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}