package com.platform.custommade.repository;

import com.platform.custommade.model.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisputeRepository extends JpaRepository<Dispute, Long> {

    // Optional: Find all disputes by order ID
    List<Dispute> findByOrderId(Long orderId);
    List<Dispute> findByRaisedById(Long userId);

}
