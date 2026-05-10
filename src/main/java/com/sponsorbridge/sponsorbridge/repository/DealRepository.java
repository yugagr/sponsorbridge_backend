package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.Deal;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    // all deals sent by a brand
    List<Deal> findByBrandId(Long brandId);

    // all deals received by an influencer
    List<Deal> findByInfluencerId(Long influencerId);

    // deals filtered by status
    List<Deal> findByInfluencerIdAndStatus(Long influencerId, DealStatus status);
    List<Deal> findByBrandIdAndStatus(Long brandId, DealStatus status);

    // count completed deals for an influencer
    long countByInfluencerIdAndStatus(Long influencerId, DealStatus status);
}