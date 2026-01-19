package com.platform.custommade.service;

import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.*;
import com.platform.custommade.repository.DisputeRepository;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisputeService {

    private final DisputeRepository disputeRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;

    public DisputeService(
            DisputeRepository disputeRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            PaymentService paymentService
    ) {
        this.disputeRepository = disputeRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
    }

    // ---------------------------------------
    // RAISE DISPUTE
    // ---------------------------------------
    public Dispute raiseDispute(
            Long orderId,
            Long raisedById,
            String reason,
            String description
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        // ✅ Order must be PAID / IN_PROGRESS / DELIVERED
        if (order.getStatus() == OrderStatus.COMPLETED ||
                order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot raise dispute for completed or cancelled order");
        }

        // ✅ Prevent duplicate dispute
        boolean exists = disputeRepository.existsByOrderId(orderId);
        if (exists) {
            throw new RuntimeException("Dispute already exists for this order");
        }

        // ✅ Payment must be HELD
        paymentService.validatePaymentHeld(orderId);

        User raisedBy = userRepository.findById(raisedById)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + raisedById));

        Dispute dispute = new Dispute();
        dispute.setOrder(order);
        dispute.setRaisedBy(raisedBy);
        dispute.setReason(reason);
        dispute.setDescription(description);
        dispute.setStatus(DisputeStatus.OPEN);
        dispute.setCreatedAt(LocalDateTime.now());

        // ✅ Update order state
        order.setStatus(OrderStatus.DISPUTE_OPENED);
        orderRepository.save(order);

        return disputeRepository.save(dispute);
    }


    // ---------------------------------------
    // GET DISPUTES
    // ---------------------------------------
    public List<Dispute> getDisputesByOrder(Long orderId) {
        return disputeRepository.findByOrderId(orderId);
    }

    public List<Dispute> getDisputesByUser(Long userId) {
        return disputeRepository.findByRaisedById(userId);
    }

    // ---------------------------------------
    // RESOLVE DISPUTE (ADMIN)
    // ---------------------------------------
    public Dispute resolveDispute(
            Long disputeId,
            DisputeStatus status,
            String adminNotes
    ) {
        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Dispute not found with id: " + disputeId));

        if (dispute.getStatus() != DisputeStatus.OPEN) {
            throw new RuntimeException("Only OPEN disputes can be resolved");
        }

        Order order = dispute.getOrder();

        // Payment already released → no escrow to act on
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new RuntimeException("Cannot resolve dispute after payment is released");
        }

        dispute.setStatus(status);
        dispute.setAdminNotes(adminNotes);

        // -----------------------------
        // RESOLUTION LOGIC
        // -----------------------------
        if (status == DisputeStatus.RESOLVED_REFUND) {
            paymentService.refundPaymentByOrder(order.getId());
            order.setStatus(OrderStatus.CANCELLED);
        }

        if (status == DisputeStatus.RESOLVED_RELEASE) {
            paymentService.releasePaymentByOrder(order.getId());
            order.setStatus(OrderStatus.COMPLETED);
        }

        orderRepository.save(order);
        return disputeRepository.save(dispute);
    }
}
