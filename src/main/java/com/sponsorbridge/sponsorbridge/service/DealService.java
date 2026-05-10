package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.dto.DealRequest;
import com.sponsorbridge.sponsorbridge.dto.DealResponse;
import com.sponsorbridge.sponsorbridge.entity.Deal;
import com.sponsorbridge.sponsorbridge.entity.Deliverable;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.enums.DeliverableStatus;
import com.sponsorbridge.sponsorbridge.repository.DealRepository;
import com.sponsorbridge.sponsorbridge.repository.DeliverableRepository;
import com.sponsorbridge.sponsorbridge.repository.UserRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final DeliverableRepository deliverableRepository;
    private final UserRepository userRepository;
    private final AuthHelper authHelper;

    // BRAND creates and sends a deal proposal to an influencer
    @Transactional
    public DealResponse createDeal(DealRequest request) {

        User brand = authHelper.getCurrentUser();

        User influencer = userRepository.findById(request.getInfluencerId())
                .orElseThrow(() -> new RuntimeException("Influencer not found"));

        Deal deal = Deal.builder()
                .brand(brand)
                .influencer(influencer)
                .title(request.getTitle())
                .description(request.getDescription())
                .budgetAmount(request.getBudgetAmount())
                .platform(request.getPlatform())
                .deadline(request.getDeadline())
                .status(DealStatus.PENDING)
                .build();

        Deal saved = dealRepository.save(deal);

        // create deliverables if brand provided them
        if (request.getDeliverableTitles() != null) {
            for (String title : request.getDeliverableTitles()) {
                Deliverable deliverable = Deliverable.builder()
                        .deal(saved)
                        .title(title)
                        .status(DeliverableStatus.PENDING)
                        .build();
                deliverableRepository.save(deliverable);
            }
        }

        return mapToResponse(saved);
    }

    // INFLUENCER accepts the deal → status becomes ACTIVE
    @Transactional
    public DealResponse acceptDeal(Long dealId) {

        User currentUser = authHelper.getCurrentUser();
        Deal deal = getDealAndVerifyInfluencer(dealId, currentUser);

        if (deal.getStatus() != DealStatus.PENDING &&
                deal.getStatus() != DealStatus.NEGOTIATING) {
            throw new RuntimeException("Deal cannot be accepted in current status: "
                    + deal.getStatus());
        }

        deal.setStatus(DealStatus.ACTIVE);
        return mapToResponse(dealRepository.save(deal));
    }

    // INFLUENCER rejects the deal
    @Transactional
    public DealResponse rejectDeal(Long dealId) {

        User currentUser = authHelper.getCurrentUser();
        Deal deal = getDealAndVerifyInfluencer(dealId, currentUser);

        if (deal.getStatus() != DealStatus.PENDING) {
            throw new RuntimeException("Only pending deals can be rejected");
        }

        deal.setStatus(DealStatus.REJECTED);
        return mapToResponse(dealRepository.save(deal));
    }

    // INFLUENCER sends a counter offer → status becomes NEGOTIATING
    @Transactional
    public DealResponse counterOffer(Long dealId, String counterOffer) {

        User currentUser = authHelper.getCurrentUser();
        Deal deal = getDealAndVerifyInfluencer(dealId, currentUser);

        if (deal.getStatus() != DealStatus.PENDING) {
            throw new RuntimeException("Counter offer only allowed on pending deals");
        }

        deal.setStatus(DealStatus.NEGOTIATING);
        deal.setCounterOffer(counterOffer);
        return mapToResponse(dealRepository.save(deal));
    }

    // BRAND cancels the deal
    @Transactional
    public DealResponse cancelDeal(Long dealId) {

        User currentUser = authHelper.getCurrentUser();
        Deal deal = getDealAndVerifyBrand(dealId, currentUser);

        if (deal.getStatus() == DealStatus.COMPLETED ||
                deal.getStatus() == DealStatus.REJECTED) {
            throw new RuntimeException("Cannot cancel a completed or rejected deal");
        }

        deal.setStatus(DealStatus.CANCELLED);
        return mapToResponse(dealRepository.save(deal));
    }

    // get all deals for the logged-in brand
    public List<DealResponse> getMyDealsAsBrand() {
        User currentUser = authHelper.getCurrentUser();
        return dealRepository.findByBrandId(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // get all deals for the logged-in influencer
    public List<DealResponse> getMyDealsAsInfluencer() {
        User currentUser = authHelper.getCurrentUser();
        return dealRepository.findByInfluencerId(currentUser.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // get single deal by id
    public DealResponse getDealById(Long dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));
        return mapToResponse(deal);
    }

    // get deals by status for influencer
    public List<DealResponse> getMyDealsByStatus(DealStatus status) {
        User currentUser = authHelper.getCurrentUser();
        return dealRepository
                .findByInfluencerIdAndStatus(currentUser.getId(), status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── private helpers ──────────────────────────────────────

    private Deal getDealAndVerifyInfluencer(Long dealId, User user) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));
        if (!deal.getInfluencer().getId().equals(user.getId())) {
            throw new RuntimeException("You are not the influencer of this deal");
        }
        return deal;
    }

    private Deal getDealAndVerifyBrand(Long dealId, User user) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));
        if (!deal.getBrand().getId().equals(user.getId())) {
            throw new RuntimeException("You are not the brand of this deal");
        }
        return deal;
    }

    // convert entity → DTO
    private DealResponse mapToResponse(Deal deal) {
        DealResponse response = new DealResponse();
        response.setId(deal.getId());
        response.setTitle(deal.getTitle());
        response.setDescription(deal.getDescription());
        response.setBudgetAmount(deal.getBudgetAmount());
        response.setPlatform(deal.getPlatform());
        response.setStatus(deal.getStatus());
        response.setDeadline(deal.getDeadline());
        response.setCounterOffer(deal.getCounterOffer());
        response.setCreatedAt(deal.getCreatedAt());
        response.setBrandId(deal.getBrand().getId());
        response.setBrandName(deal.getBrand().getName());
        response.setInfluencerId(deal.getInfluencer().getId());
        response.setInfluencerName(deal.getInfluencer().getName());
        return response;
    }
}