package com.platform.custommade.service;

import com.platform.custommade.dto.request.CreatePaymentDTO;
import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.model.Payment;
import com.platform.custommade.model.PaymentStatus;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    // --------------------------------------------------
    // CREATE PAYMENT (ESCROW HOLD)
    // --------------------------------------------------
    public Payment createPayment(CreatePaymentDTO dto) {

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + dto.getOrderId()
                        ));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Cannot create payment for completed order");
        }

        // ❗ Only one payment per order
        if (paymentRepository.existsByOrderId(order.getId())) {
            throw new RuntimeException("Payment already exists for this order");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(BigDecimal.valueOf(dto.getAmount()));
        payment.setPaymentGatewayId(dto.getPaymentGatewayId());
        payment.setStatus(PaymentStatus.HELD);
        payment.setCreatedAt(LocalDateTime.now());

        // ✅ IMPORTANT: update order status when payment is created
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }

    // --------------------------------------------------
    // GET PAYMENTS BY CUSTOMER
    // --------------------------------------------------
    public List<Payment> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByOrderCustomerId(customerId);
    }

    // --------------------------------------------------
    // GET ALL PAYMENTS (ADMIN)
    // --------------------------------------------------
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // --------------------------------------------------
    // GET SINGLE PAYMENT
    // --------------------------------------------------
    public Payment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + paymentId
                        ));
    }

    // --------------------------------------------------
    // UPDATE PAYMENT STATUS (INTERNAL)
    // --------------------------------------------------
    private Payment updatePaymentStatus(Long paymentId, PaymentStatus newStatus) {

        Payment payment = getPaymentById(paymentId);

        if (payment.getStatus() != PaymentStatus.HELD) {
            throw new RuntimeException("Only HELD payments can be updated");
        }

        payment.setStatus(newStatus);

        Order order = payment.getOrder();

        if (newStatus == PaymentStatus.RELEASED) {
            order.setStatus(OrderStatus.PAID);
        } else if (newStatus == PaymentStatus.REFUNDED) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
        return paymentRepository.save(payment);
    }

    // --------------------------------------------------
    // RELEASE PAYMENT
    // --------------------------------------------------
    public Payment releasePayment(Long paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.RELEASED);
    }

    // --------------------------------------------------
    // REFUND PAYMENT
    // --------------------------------------------------
    public Payment refundPayment(Long paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.REFUNDED);
    }

    // --------------------------------------------------
    // RELEASE PAYMENT BY ORDER (USED IN DISPUTE FLOW)
    // --------------------------------------------------
    public Payment releasePaymentByOrder(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found for order " + orderId
                        ));

        return releasePayment(payment.getId());
    }

    // --------------------------------------------------
    // REFUND PAYMENT BY ORDER (USED IN DISPUTE FLOW)
    // --------------------------------------------------
    public Payment refundPaymentByOrder(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found for order " + orderId
                        ));

        return refundPayment(payment.getId());
    }

    public void validatePaymentHeld(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found for order"));

        if (payment.getStatus() != PaymentStatus.HELD) {
            throw new RuntimeException("Dispute allowed only when payment is HELD");
        }
    }

}
