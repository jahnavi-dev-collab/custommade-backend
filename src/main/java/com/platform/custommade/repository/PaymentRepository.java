package com.platform.custommade.repository;

import com.platform.custommade.model.Payment;
import com.platform.custommade.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Optional: find payments by order
    List<Payment> findByOrderId(Long orderId);

    // Optional: find payments by status
    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByOrderCustomerId(Long customerId);

}
