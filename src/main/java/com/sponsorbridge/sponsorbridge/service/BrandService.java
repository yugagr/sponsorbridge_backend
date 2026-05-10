package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.dto.BrandProfileRequest;
import com.sponsorbridge.sponsorbridge.dto.BrandProfileResponse;
import com.sponsorbridge.sponsorbridge.entity.BrandProfile;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.repository.BrandRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final AuthHelper authHelper;

    public BrandProfileResponse createProfile(BrandProfileRequest request) {

        User currentUser = authHelper.getCurrentUser();

        if (brandRepository.findByUserId(currentUser.getId()).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        BrandProfile profile = BrandProfile.builder()
                .user(currentUser)
                .companyName(request.getCompanyName())
                .industry(request.getIndustry())
                .description(request.getDescription())
                .website(request.getWebsite())
                .totalSpent(0.0)
                .totalDealsCreated(0)
                .build();

        BrandProfile saved = brandRepository.save(profile);
        return mapToResponse(saved);
    }

    public BrandProfileResponse updateProfile(BrandProfileRequest request) {

        User currentUser = authHelper.getCurrentUser();

        BrandProfile profile = brandRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setCompanyName(request.getCompanyName());
        profile.setIndustry(request.getIndustry());
        profile.setDescription(request.getDescription());
        profile.setWebsite(request.getWebsite());

        return mapToResponse(brandRepository.save(profile));
    }

    public BrandProfileResponse getMyProfile() {
        User currentUser = authHelper.getCurrentUser();
        BrandProfile profile = brandRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToResponse(profile);
    }

    private BrandProfileResponse mapToResponse(BrandProfile profile) {
        BrandProfileResponse response = new BrandProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setName(profile.getUser().getName());
        response.setEmail(profile.getUser().getEmail());
        response.setCompanyName(profile.getCompanyName());
        response.setIndustry(profile.getIndustry());
        response.setDescription(profile.getDescription());
        response.setWebsite(profile.getWebsite());
        response.setTotalSpent(profile.getTotalSpent());
        response.setTotalDealsCreated(profile.getTotalDealsCreated());
        return response;
    }
}