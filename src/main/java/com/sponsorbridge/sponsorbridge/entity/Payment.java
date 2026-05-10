package com.sponsorbridge.sponsorbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sponsorbridge.sponsorbridge.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne                           // one payment belongs to one deal
    @JoinColumn(name = "deal_id", nullable = false, unique = true)
    private Deal deal;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;       // PENDING, RELEASED, DISPUTED

    private LocalDateTime releasedAt;   // when brand released the payment

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = PaymentStatus.PENDING;
    }
}
