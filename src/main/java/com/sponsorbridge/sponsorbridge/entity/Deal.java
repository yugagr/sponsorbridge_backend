package com.sponsorbridge.sponsorbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "deals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne                          // many deals can belong to one brand
    @JoinColumn(name = "brand_id", nullable = false)
    private User brand;                 // the brand who created this deal

    @ManyToOne                          // many deals can belong to one influencer
    @JoinColumn(name = "influencer_id", nullable = false)
    private User influencer;            // the influencer this deal is sent to

    @Column(nullable = false)
    private String title;               // e.g. "Promote our new headphones"

    @Column(length = 1000)
    private String description;         // full details of what brand wants

    private Double budgetAmount;        // how much brand is offering

    @Enumerated(EnumType.STRING)
    private Platform platform;          // which platform the content is for

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DealStatus status;          // current stage of the deal

    private LocalDate deadline;         // when deliverables must be completed

    private String counterOffer;        // influencer's counter proposal if negotiating

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // one deal has many deliverables
    // CascadeType.ALL = if deal is deleted, delete its deliverables too
    @JsonIgnore
    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deliverable> deliverables;

    // one deal has one payment
    @JsonIgnore
    @OneToOne(mappedBy = "deal", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = DealStatus.PENDING;  // default status
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
