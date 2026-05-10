package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.entity.Payment;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.PaymentStatus;
import com.sponsorbridge.sponsorbridge.repository.PaymentRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AuthHelper authHelper;

    // BRAND releases payment after deal is completed
    @Transactional
    public Payment releasePayment(Long dealId) {

        User currentUser = authHelper.getCurrentUser();

        Payment payment = paymentRepository.findByDealId(dealId)
                .orElseThrow(() -> new RuntimeException("Payment record not found"));

        // only the brand of this deal can release payment
        if (!payment.getDeal().getBrand().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the brand can release payment");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Payment already released or disputed");
        }

        payment.setStatus(PaymentStatus.RELEASED);
        payment.setReleasedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    // INFLUENCER disputes a payment
    @Transactional
    public Payment disputePayment(Long dealId) {

        User currentUser = authHelper.getCurrentUser();

        Payment payment = paymentRepository.findByDealId(dealId)
                .orElseThrow(() -> new RuntimeException("Payment record not found"));

        if (!payment.getDeal().getInfluencer().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only the influencer can dispute payment");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Only pending payments can be disputed");
        }

        payment.setStatus(PaymentStatus.DISPUTED);
        return paymentRepository.save(payment);
    }

    // get payment details for a deal
    public Payment getPaymentByDeal(Long dealId) {
        return paymentRepository.findByDealId(dealId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    // get total earnings for logged-in influencer
    public Double getMyTotalEarnings() {
        User currentUser = authHelper.getCurrentUser();
        Double earnings = paymentRepository
                .getTotalEarningsByInfluencer(currentUser.getId());
        return earnings != null ? earnings : 0.0;
    }

    // get total spent for logged-in brand
    public Double getMyTotalSpent() {
        User currentUser = authHelper.getCurrentUser();
        Double spent = paymentRepository
                .getTotalSpentByBrand(currentUser.getId());
        return spent != null ? spent : 0.0;
    }
}