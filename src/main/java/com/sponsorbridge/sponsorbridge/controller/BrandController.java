package com.sponsorbridge.sponsorbridge.controller;

import com.sponsorbridge.sponsorbridge.dto.BrandProfileRequest;
import com.sponsorbridge.sponsorbridge.dto.BrandProfileResponse;
import com.sponsorbridge.sponsorbridge.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    // POST /api/brands/profile
    @PostMapping("/profile")
    public ResponseEntity<BrandProfileResponse> createProfile(
            @Valid @RequestBody BrandProfileRequest request) {
        return ResponseEntity.ok(brandService.createProfile(request));
    }

    // PUT /api/brands/profile
    @PutMapping("/profile")
    public ResponseEntity<BrandProfileResponse> updateProfile(
            @Valid @RequestBody BrandProfileRequest request) {
        return ResponseEntity.ok(brandService.updateProfile(request));
    }

    // GET /api/brands/profile/me
    @GetMapping("/profile/me")
    public ResponseEntity<BrandProfileResponse> getMyProfile() {
        return ResponseEntity.ok(brandService.getMyProfile());
    }
}