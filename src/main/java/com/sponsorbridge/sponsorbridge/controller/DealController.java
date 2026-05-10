package com.sponsorbridge.sponsorbridge.controller;

import com.sponsorbridge.sponsorbridge.dto.DealRequest;
import com.sponsorbridge.sponsorbridge.dto.DealResponse;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.service.DealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    // BRAND sends deal proposal
    // POST /api/deals
    @PostMapping
    public ResponseEntity<DealResponse> createDeal(
            @Valid @RequestBody DealRequest request) {
        return ResponseEntity.ok(dealService.createDeal(request));
    }

    // INFLUENCER accepts deal
    // PUT /api/deals/{id}/accept
    @PutMapping("/{id}/accept")
    public ResponseEntity<DealResponse> acceptDeal(@PathVariable Long id) {
        return ResponseEntity.ok(dealService.acceptDeal(id));
    }

    // INFLUENCER rejects deal
    // PUT /api/deals/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<DealResponse> rejectDeal(@PathVariable Long id) {
        return ResponseEntity.ok(dealService.rejectDeal(id));
    }

    // INFLUENCER sends counter offer
    // PUT /api/deals/{id}/counter
    @PutMapping("/{id}/counter")
    public ResponseEntity<DealResponse> counterOffer(
            @PathVariable Long id,
            @RequestParam String offer) {
        return ResponseEntity.ok(dealService.counterOffer(id, offer));
    }

    // BRAND cancels deal
    // PUT /api/deals/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<DealResponse> cancelDeal(@PathVariable Long id) {
        return ResponseEntity.ok(dealService.cancelDeal(id));
    }

    // GET all deals for brand
    // GET /api/deals/brand
    @GetMapping("/brand")
    public ResponseEntity<List<DealResponse>> getMyDealsAsBrand() {
        return ResponseEntity.ok(dealService.getMyDealsAsBrand());
    }

    // GET all deals for influencer
    // GET /api/deals/influencer
    @GetMapping("/influencer")
    public ResponseEntity<List<DealResponse>> getMyDealsAsInfluencer() {
        return ResponseEntity.ok(dealService.getMyDealsAsInfluencer());
    }

    // GET single deal
    // GET /api/deals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<DealResponse> getDealById(@PathVariable Long id) {
        return ResponseEntity.ok(dealService.getDealById(id));
    }

    // GET deals by status for influencer
    // GET /api/deals/influencer/status?status=PENDING
    @GetMapping("/influencer/status")
    public ResponseEntity<List<DealResponse>> getDealsByStatus(
            @RequestParam DealStatus status) {
        return ResponseEntity.ok(dealService.getMyDealsByStatus(status));
    }
}