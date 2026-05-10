package com.sponsorbridge.sponsorbridge.controller;

import com.sponsorbridge.sponsorbridge.dto.InfluencerProfileRequest;
import com.sponsorbridge.sponsorbridge.dto.InfluencerProfileResponse;
import com.sponsorbridge.sponsorbridge.enums.Niche;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import com.sponsorbridge.sponsorbridge.service.InfluencerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/influencers")
@RequiredArgsConstructor
public class InfluencerController {

    private final InfluencerService influencerService;

    // POST /api/influencers/profile
    @PostMapping("/profile")
    public ResponseEntity<InfluencerProfileResponse> createProfile(
            @Valid @RequestBody InfluencerProfileRequest request) {
        return ResponseEntity.ok(influencerService.createProfile(request));
    }

    // PUT /api/influencers/profile
    @PutMapping("/profile")
    public ResponseEntity<InfluencerProfileResponse> updateProfile(
            @Valid @RequestBody InfluencerProfileRequest request) {
        return ResponseEntity.ok(influencerService.updateProfile(request));
    }

    // GET /api/influencers/profile/me
    @GetMapping("/profile/me")
    public ResponseEntity<InfluencerProfileResponse> getMyProfile() {
        return ResponseEntity.ok(influencerService.getMyProfile());
    }

    // GET /api/influencers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<InfluencerProfileResponse> getProfileById(
            @PathVariable Long id) {
        return ResponseEntity.ok(influencerService.getProfileById(id));
    }

    // GET /api/influencers?platform=YOUTUBE&niche=TECH&maxPrice=50000
    @GetMapping
    public ResponseEntity<List<InfluencerProfileResponse>> searchInfluencers(
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) Niche niche,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(
                influencerService.searchInfluencers(platform, niche, maxPrice));
    }
}