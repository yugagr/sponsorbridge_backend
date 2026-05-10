package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.InfluencerProfile;
import com.sponsorbridge.sponsorbridge.enums.Niche;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfluencerRepository extends JpaRepository<InfluencerProfile, Long> {

    // find profile by user id
    Optional<InfluencerProfile> findByUserId(Long userId);

    // filter by platform
    List<InfluencerProfile> findByPlatform(Platform platform);

    // filter by niche
    List<InfluencerProfile> findByNiche(Niche niche);

    // filter by platform AND niche together
    List<InfluencerProfile> findByPlatformAndNiche(Platform platform, Niche niche);

    // find influencers whose price is within brand's budget
    List<InfluencerProfile> findByPricePerPostLessThanEqual(Double budget);

    // find influencers with minimum followers
    List<InfluencerProfile> findByFollowerCountGreaterThanEqual(Long minFollowers);

    // search influencers by budget + platform + niche (for search feature)
    @Query("SELECT i FROM InfluencerProfile i WHERE " +
            "(:platform IS NULL OR i.platform = :platform) AND " +
            "(:niche IS NULL OR i.niche = :niche) AND " +
            "(:maxPrice IS NULL OR i.pricePerPost <= :maxPrice)")
    List<InfluencerProfile> searchInfluencers(
            @Param("platform") Platform platform,
            @Param("niche") Niche niche,
            @Param("maxPrice") Double maxPrice);
}
