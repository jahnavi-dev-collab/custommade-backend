package com.platform.custommade.service;

import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.model.Payment;
import com.platform.custommade.model.PaymentStatus;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    // -------------------------------
    // Create a payment for an order (hold in escrow)
    // -------------------------------
    public Payment createPayment(Long orderId, Double amount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.HELD);  // money held in escrow
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // Update order status to PAID
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return savedPayment;
    }

    // -------------------------------
    // Update payment status (release or refund)
    // -------------------------------
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        payment.setStatus(status);

        // Optionally update order status if needed
        Order order = payment.getOrder();
        if (status == PaymentStatus.RELEASED) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        } else if (status == PaymentStatus.REFUNDED) {
            order.setStatus(OrderStatus.DISPUTE_OPENED); // example
            orderRepository.save(order);
        }

        return paymentRepository.save(payment);
    }

    // -------------------------------
    // Get all payments for a customer
    // -------------------------------
    public List<Payment> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByOrderCustomerId(customerId);
    }

    // -------------------------------
    // Get all payments
    // -------------------------------
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // -------------------------------
    // Optional: release and refund methods
    // -------------------------------
    public Payment releasePayment(Long paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.RELEASED);
    }

    public Payment refundPayment(Long paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.REFUNDED);
    }

    // -------------------------------
    // Get a single payment
    // -------------------------------
    public Optional<Payment> getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }
}
