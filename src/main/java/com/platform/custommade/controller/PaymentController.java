package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreatePaymentDTO;
import com.platform.custommade.dto.response.PaymentResponseDTO;
import com.platform.custommade.model.Payment;
import com.platform.custommade.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // --------------------------------------------------
    // CREATE PAYMENT (ESCROW HOLD)
    // --------------------------------------------------
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody CreatePaymentDTO dto) {

        Payment payment = paymentService.createPayment(dto);
        return ResponseEntity.ok(toResponse(payment));
    }

    // --------------------------------------------------
    // GET PAYMENTS BY CUSTOMER
    // --------------------------------------------------
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByCustomer(
            @PathVariable Long customerId) {

        List<PaymentResponseDTO> response = paymentService
                .getPaymentsByCustomer(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // ADMIN: GET ALL PAYMENTS
    // --------------------------------------------------
    @GetMapping("/all")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {

        List<PaymentResponseDTO> response = paymentService
                .getAllPayments()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // --------------------------------------------------
    // RELEASE PAYMENT (ADMIN / DISPUTE RESOLUTION)
    // --------------------------------------------------
    @PutMapping("/{paymentId}/release")
    public ResponseEntity<PaymentResponseDTO> releasePayment(
            @PathVariable Long paymentId) {

        Payment payment = paymentService.releasePayment(paymentId);
        return ResponseEntity.ok(toResponse(payment));
    }

    // --------------------------------------------------
    // REFUND PAYMENT (ADMIN / DISPUTE RESOLUTION)
    // --------------------------------------------------
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(
            @PathVariable Long paymentId) {

        Payment payment = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(toResponse(payment));
    }

    // --------------------------------------------------
    // GET SINGLE PAYMENT BY ID
    // --------------------------------------------------
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getPayment(
            @PathVariable Long paymentId) {

        Payment payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(toResponse(payment));
    }

    // --------------------------------------------------
    // MAPPER
    // --------------------------------------------------
    private PaymentResponseDTO toResponse(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount().doubleValue());
        dto.setStatus(payment.getStatus().name());
        dto.setPaymentGatewayId(payment.getPaymentGatewayId());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}
