package com.platform.custommade.service;

import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.Dispute;
import com.platform.custommade.model.DisputeStatus;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.User;
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

    public DisputeService(
            DisputeRepository disputeRepository,
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.disputeRepository = disputeRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // ✅ Raise a dispute
    public Dispute raiseDispute(Long orderId, Long raisedById, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User raisedBy = userRepository.findById(raisedById)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Dispute dispute = new Dispute();
        dispute.setOrder(order);
        dispute.setRaisedBy(raisedBy);
        dispute.setReason(reason);
        dispute.setStatus(DisputeStatus.OPEN);
        dispute.setCreatedAt(LocalDateTime.now());

        return disputeRepository.save(dispute);
    }

    // ✅ Get disputes for an order
    public List<Dispute> getDisputesByOrder(Long orderId) {
        return disputeRepository.findByOrderId(orderId);
    }

    // ✅ Get disputes raised by a user
    public List<Dispute> getDisputesByUser(Long userId) {
        return disputeRepository.findByRaisedById(userId);
    }

    // ✅ Resolve a dispute (admin)
    public Dispute resolveDispute(Long disputeId, String resolution, String adminNotes) {
        Dispute dispute = disputeRepository.findById(disputeId)
                .orElseThrow(() -> new ResourceNotFoundException("Dispute not found"));

        // Set status based on resolution string (REFUND / RELEASE)
        dispute.setStatus(DisputeStatus.valueOf(resolution));
        dispute.setAdminNotes(adminNotes);

        return disputeRepository.save(dispute);
    }
}
