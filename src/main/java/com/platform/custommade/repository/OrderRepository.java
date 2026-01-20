package com.platform.custommade.repository;

import com.platform.custommade.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByQuoteId(Long quoteId);

    boolean existsByQuoteId(Long quoteId);

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByTailorId(Long tailorId);
}
