package com.platform.custommade.repository;

import com.platform.custommade.model.Request;
import com.platform.custommade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    // Get all requests created by a customer
    List<Request> findByCustomerId(Long customerId);

    // Optional: filter by status (OPEN / CLOSED)
    List<Request> findByStatus(String status);
}
