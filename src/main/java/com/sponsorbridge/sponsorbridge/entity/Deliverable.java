package com.sponsorbridge.sponsorbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sponsorbridge.sponsorbridge.enums.DeliverableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deliverables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deliverable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne                          // many deliverables belong to one deal
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @Column(nullable = false)
    private String title;               // e.g. "Post 1 YouTube video"

    private String description;         // more details about this task

    @Enumerated(EnumType.STRING)
    private DeliverableStatus status;   // PENDING, SUBMITTED, APPROVED, REJECTED

    private String submissionLink;      // influencer pastes YouTube/Instagram link here

    @PrePersist
    protected void onCreate() {
        if (status == null) status = DeliverableStatus.PENDING;
    }
}