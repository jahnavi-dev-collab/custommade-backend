package com.platform.custommade.repository;

import com.platform.custommade.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    // Find all measurements for a specific request
    List<Measurement> findByRequestId(Long requestId);
}
