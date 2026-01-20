package com.platform.custommade.repository;

import com.platform.custommade.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findByRequestId(Long requestId);

    List<Quote> findByTailorId(Long tailorId);

}
