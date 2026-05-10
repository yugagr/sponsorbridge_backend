package com.sponsorbridge.sponsorbridge.controller;

import com.sponsorbridge.sponsorbridge.entity.Payment;
import com.sponsorbridge.sponsorbridge.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // BRAND releases payment
    // PUT /api/payments/deal/{dealId}/release
    @PutMapping("/deal/{dealId}/release")
    public ResponseEntity<Payment> releasePayment(@PathVariable Long dealId) {
        return ResponseEntity.ok(paymentService.releasePayment(dealId));
    }

    // INFLUENCER disputes payment
    // PUT /api/payments/deal/{dealId}/dispute
    @PutMapping("/deal/{dealId}/dispute")
    public ResponseEntity<Payment> disputePayment(@PathVariable Long dealId) {
        return ResponseEntity.ok(paymentService.disputePayment(dealId));
    }

    // GET payment for a deal
    // GET /api/payments/deal/{dealId}
    @GetMapping("/deal/{dealId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long dealId) {
        return ResponseEntity.ok(paymentService.getPaymentByDeal(dealId));
    }

    // GET my total earnings (influencer)
    // GET /api/payments/earnings
    @GetMapping("/earnings")
    public ResponseEntity<Double> getMyEarnings() {
        return ResponseEntity.ok(paymentService.getMyTotalEarnings());
    }

    // GET my total spent (brand)
    // GET /api/payments/spent
    @GetMapping("/spent")
    public ResponseEntity<Double> getMySpent() {
        return ResponseEntity.ok(paymentService.getMyTotalSpent());
    }
}