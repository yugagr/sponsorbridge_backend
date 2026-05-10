package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByDealId(Long dealId);

    // total earnings for an influencer
    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "JOIN p.deal d WHERE d.influencer.id = :influencerId " +
            "AND p.status = 'RELEASED'")
    Double getTotalEarningsByInfluencer(@Param("influencerId") Long influencerId);

    // total spent by a brand
    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "JOIN p.deal d WHERE d.brand.id = :brandId " +
            "AND p.status = 'RELEASED'")
    Double getTotalSpentByBrand(@Param("brandId") Long brandId);
}
