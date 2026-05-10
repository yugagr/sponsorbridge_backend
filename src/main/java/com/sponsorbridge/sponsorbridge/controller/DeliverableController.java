package com.sponsorbridge.sponsorbridge.controller;

import com.sponsorbridge.sponsorbridge.entity.Deliverable;
import com.sponsorbridge.sponsorbridge.service.DeliverableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/deliverables")
@RequiredArgsConstructor
public class DeliverableController {

    private final DeliverableService deliverableService;

    // INFLUENCER submits deliverable with link
    // PUT /api/deliverables/{id}/submit?link=https://youtube.com/xyz
    @PutMapping("/{id}/submit")
    public ResponseEntity<Deliverable> submitDeliverable(
            @PathVariable Long id,
            @RequestParam String link) {
        return ResponseEntity.ok(deliverableService.submitDeliverable(id, link));
    }

    // BRAND approves deliverable
    // PUT /api/deliverables/{id}/approve
    @PutMapping("/{id}/approve")
    public ResponseEntity<Deliverable> approveDeliverable(@PathVariable Long id) {
        return ResponseEntity.ok(deliverableService.approveDeliverable(id));
    }

    // BRAND rejects deliverable
    // PUT /api/deliverables/{id}/reject?reason=Video quality too low
    @PutMapping("/{id}/reject")
    public ResponseEntity<Deliverable> rejectDeliverable(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(deliverableService.rejectDeliverable(id, reason));
    }

    // GET all deliverables for a deal
    // GET /api/deliverables/deal/{dealId}
    @GetMapping("/deal/{dealId}")
    public ResponseEntity<List<Deliverable>> getDeliverablesByDeal(
            @PathVariable Long dealId) {
        return ResponseEntity.ok(deliverableService.getDeliverablesByDeal(dealId));
    }
}