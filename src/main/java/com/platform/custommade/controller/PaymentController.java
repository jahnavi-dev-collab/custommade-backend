package com.platform.custommade.controller;

import com.platform.custommade.model.Payment;
import com.platform.custommade.model.PaymentStatus;
import com.platform.custommade.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // -------------------------------
    // Create a payment for an order
    // -------------------------------
    @PostMapping("/create")
    public ResponseEntity<Payment> createPayment(
            @RequestParam Long orderId,
            @RequestParam Double amount) {

        Payment payment = paymentService.createPayment(orderId, amount);
        return ResponseEntity.ok(payment);
    }

    // -------------------------------
    // Get all payments for a customer
    // -------------------------------
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Payment>> getPaymentsByCustomer(@PathVariable Long customerId) {
        List<Payment> payments = paymentService.getPaymentsByCustomer(customerId);
        return ResponseEntity.ok(payments);
    }

    // -------------------------------
    // Get all payments (admin)
    // -------------------------------
    @GetMapping("/all")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // -------------------------------
    // Update payment status
    // -------------------------------
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long paymentId,
            @RequestParam PaymentStatus status) {

        Payment payment = paymentService.updatePaymentStatus(paymentId, status);
        return ResponseEntity.ok(payment);
    }

    // -------------------------------
    // Release payment (shortcut)
    // -------------------------------
    @PutMapping("/{paymentId}/release")
    public ResponseEntity<Payment> releasePayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.releasePayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    // -------------------------------
    // Refund payment (shortcut)
    // -------------------------------
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    // -------------------------------
    // Get a single payment by ID
    // -------------------------------
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.getPayment(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        return ResponseEntity.ok(payment);
    }
}
