package com.platform.custommade.repository;

import com.platform.custommade.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    // Find all quotes for a specific request
    List<Quote> findByRequestId(Long requestId);

    // Find all quotes by a specific tailor
    List<Quote> findByTailorId(Long tailorId);
}
