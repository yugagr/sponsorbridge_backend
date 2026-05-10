package com.sponsorbridge.sponsorbridge.enums;

public enum DealStatus {
    PENDING,       // brand sent offer, influencer hasn't responded
    NEGOTIATING,   // influencer sent counter offer
    ACTIVE,        // both agreed, work in progress
    COMPLETED,     // all deliverables done, payment released
    REJECTED,      // influencer rejected the offer
    CANCELLED      // cancelled after being active
}
