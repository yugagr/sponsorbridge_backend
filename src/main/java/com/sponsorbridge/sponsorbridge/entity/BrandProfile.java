package com.sponsorbridge.sponsorbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "brand_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String companyName;

    private String industry;            // e.g. Electronics, Fashion, Food

    @Column(length = 500)
    private String description;

    private String website;

    private Double totalSpent;          // total money spent on deals

    private Integer totalDealsCreated;
}