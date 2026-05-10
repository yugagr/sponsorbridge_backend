package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.BrandProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandProfile, Long> {

    Optional<BrandProfile> findByUserId(Long userId);
}
