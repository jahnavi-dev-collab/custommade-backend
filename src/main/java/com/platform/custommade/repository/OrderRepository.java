package com.platform.custommade.repository;

import com.platform.custommade.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByTailorId(Long tailorId);

    Optional<Order> findByRequestId(Long requestId);

    Optional<Order> findByQuoteId(Long quoteId);

}

