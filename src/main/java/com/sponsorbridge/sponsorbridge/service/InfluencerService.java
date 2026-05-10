package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.dto.InfluencerProfileRequest;
import com.sponsorbridge.sponsorbridge.dto.InfluencerProfileResponse;
import com.sponsorbridge.sponsorbridge.entity.InfluencerProfile;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.Niche;
import com.sponsorbridge.sponsorbridge.enums.Platform;
import com.sponsorbridge.sponsorbridge.repository.InfluencerRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfluencerService {

    private final InfluencerRepository influencerRepository;
    private final AuthHelper authHelper;

    // create profile after registration
    public InfluencerProfileResponse createProfile(InfluencerProfileRequest request) {

        User currentUser = authHelper.getCurrentUser();

        // check if profile already exists
        if (influencerRepository.findByUserId(currentUser.getId()).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        InfluencerProfile profile = InfluencerProfile.builder()
                .user(currentUser)
                .platform(request.getPlatform())
                .niche(request.getNiche())
                .followerCount(request.getFollowerCount())
                .pricePerPost(request.getPricePerPost())
                .bio(request.getBio())
                .channelOrHandle(request.getChannelOrHandle())
                .averageRating(0.0)
                .totalDealsCompleted(0)
                .build();

        InfluencerProfile saved = influencerRepository.save(profile);
        return mapToResponse(saved);
    }

    // update existing profile
    public InfluencerProfileResponse updateProfile(InfluencerProfileRequest request) {

        User currentUser = authHelper.getCurrentUser();

        InfluencerProfile profile = influencerRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setPlatform(request.getPlatform());
        profile.setNiche(request.getNiche());
        profile.setFollowerCount(request.getFollowerCount());
        profile.setPricePerPost(request.getPricePerPost());
        profile.setBio(request.getBio());
        profile.setChannelOrHandle(request.getChannelOrHandle());

        InfluencerProfile updated = influencerRepository.save(profile);
        return mapToResponse(updated);
    }

    // get my own profile
    public InfluencerProfileResponse getMyProfile() {
        User currentUser = authHelper.getCurrentUser();
        InfluencerProfile profile = influencerRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToResponse(profile);
    }

    // get any influencer profile by id (for brands to view)
    public InfluencerProfileResponse getProfileById(Long id) {
        InfluencerProfile profile = influencerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Influencer not found"));
        return mapToResponse(profile);
    }

    // search influencers — brands use this
    public List<InfluencerProfileResponse> searchInfluencers(
            Platform platform, Niche niche, Double maxPrice) {

        return influencerRepository
                .searchInfluencers(platform, niche, maxPrice)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // get all influencers
    public List<InfluencerProfileResponse> getAllInfluencers() {
        return influencerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // convert entity → response DTO
    // we do this to avoid exposing raw entity with password etc.
    private InfluencerProfileResponse mapToResponse(InfluencerProfile profile) {
        InfluencerProfileResponse response = new InfluencerProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setName(profile.getUser().getName());
        response.setEmail(profile.getUser().getEmail());
        response.setPlatform(profile.getPlatform());
        response.setNiche(profile.getNiche());
        response.setFollowerCount(profile.getFollowerCount());
        response.setPricePerPost(profile.getPricePerPost());
        response.setBio(profile.getBio());
        response.setChannelOrHandle(profile.getChannelOrHandle());
        response.setAverageRating(profile.getAverageRating());
        response.setTotalDealsCompleted(profile.getTotalDealsCompleted());
        return response;
    }
}