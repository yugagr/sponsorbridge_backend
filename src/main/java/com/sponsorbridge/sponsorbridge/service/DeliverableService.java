package com.sponsorbridge.sponsorbridge.service;

import com.sponsorbridge.sponsorbridge.entity.Deal;
import com.sponsorbridge.sponsorbridge.entity.Deliverable;
import com.sponsorbridge.sponsorbridge.entity.Payment;
import com.sponsorbridge.sponsorbridge.entity.User;
import com.sponsorbridge.sponsorbridge.enums.DealStatus;
import com.sponsorbridge.sponsorbridge.enums.DeliverableStatus;
import com.sponsorbridge.sponsorbridge.enums.PaymentStatus;
import com.sponsorbridge.sponsorbridge.repository.DealRepository;
import com.sponsorbridge.sponsorbridge.repository.DeliverableRepository;
import com.sponsorbridge.sponsorbridge.repository.PaymentRepository;
import com.sponsorbridge.sponsorbridge.security.AuthHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliverableService {

    private final DeliverableRepository deliverableRepository;
    private final DealRepository dealRepository;
    private final PaymentRepository paymentRepository;
    private final AuthHelper authHelper;

    // INFLUENCER submits a deliverable with a link
    @Transactional
    public Deliverable submitDeliverable(Long deliverableId, String submissionLink) {

        User currentUser = authHelper.getCurrentUser();

        Deliverable deliverable = deliverableRepository.findById(deliverableId)
                .orElseThrow(() -> new RuntimeException("Deliverable not found"));

        // make sure this influencer owns this deliverable
        if (!deliverable.getDeal().getInfluencer().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException("You are not the influencer of this deal");
        }

        if (deliverable.getStatus() != DeliverableStatus.PENDING &&
                deliverable.getStatus() != DeliverableStatus.REJECTED) {
            throw new RuntimeException("Deliverable cannot be submitted in status: "
                    + deliverable.getStatus());
        }

        deliverable.setSubmissionLink(submissionLink);
        deliverable.setStatus(DeliverableStatus.SUBMITTED);
        return deliverableRepository.save(deliverable);
    }

    // BRAND approves a deliverable
    @Transactional
    public Deliverable approveDeliverable(Long deliverableId) {

        User currentUser = authHelper.getCurrentUser();

        Deliverable deliverable = deliverableRepository.findById(deliverableId)
                .orElseThrow(() -> new RuntimeException("Deliverable not found"));

        if (!deliverable.getDeal().getBrand().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException("You are not the brand of this deal");
        }

        if (deliverable.getStatus() != DeliverableStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted deliverables can be approved");
        }

        deliverable.setStatus(DeliverableStatus.APPROVED);
        Deliverable saved = deliverableRepository.save(deliverable);

        // check if ALL deliverables for this deal are now approved
        // if yes → auto complete the deal
        checkAndCompleteDeal(deliverable.getDeal());

        return saved;
    }

    // BRAND rejects a deliverable — influencer must resubmit
    @Transactional
    public Deliverable rejectDeliverable(Long deliverableId, String reason) {

        User currentUser = authHelper.getCurrentUser();

        Deliverable deliverable = deliverableRepository.findById(deliverableId)
                .orElseThrow(() -> new RuntimeException("Deliverable not found"));

        if (!deliverable.getDeal().getBrand().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException("You are not the brand of this deal");
        }

        if (deliverable.getStatus() != DeliverableStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted deliverables can be rejected");
        }

        // set back to PENDING with a reason in description
        deliverable.setStatus(DeliverableStatus.REJECTED);
        deliverable.setDescription("Rejected: " + reason);
        return deliverableRepository.save(deliverable);
    }

    // get all deliverables for a deal
    public List<Deliverable> getDeliverablesByDeal(Long dealId) {
        return deliverableRepository.findByDealId(dealId);
    }

    // ── private helper ───────────────────────────────────────

    // called after every approval — checks if deal is fully done
    private void checkAndCompleteDeal(Deal deal) {

        // existsByDealIdAndStatusNot checks if ANY deliverable is NOT approved
        boolean anyNotApproved = deliverableRepository
                .existsByDealIdAndStatusNot(deal.getId(), DeliverableStatus.APPROVED);

        if (!anyNotApproved) {
            // ALL deliverables approved → mark deal as COMPLETED
            deal.setStatus(DealStatus.COMPLETED);
            dealRepository.save(deal);

            // auto create payment record
            Payment payment = Payment.builder()
                    .deal(deal)
                    .amount(deal.getBudgetAmount())
                    .status(PaymentStatus.PENDING)  // brand still needs to release it
                    .build();
            paymentRepository.save(payment);
        }
    }
}