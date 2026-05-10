package com.sponsorbridge.sponsorbridge.repository;

import com.sponsorbridge.sponsorbridge.entity.Deliverable;
import com.sponsorbridge.sponsorbridge.enums.DeliverableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliverableRepository extends JpaRepository<Deliverable, Long> {

    List<Deliverable> findByDealId(Long dealId);

    List<Deliverable> findByDealIdAndStatus(Long dealId, DeliverableStatus status);

    // check if all deliverables for a deal are approved
    boolean existsByDealIdAndStatusNot(Long dealId, DeliverableStatus status);
}
