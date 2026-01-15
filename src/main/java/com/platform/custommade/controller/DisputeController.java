package com.platform.custommade.controller;

import com.platform.custommade.model.Dispute;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.User;
import com.platform.custommade.service.DisputeService;
import com.platform.custommade.service.OrderService;
import com.platform.custommade.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disputes")
public class DisputeController {

    private final DisputeService disputeService;
    private final OrderService orderService;
    private final UserService userService;

    public DisputeController(
            DisputeService disputeService,
            OrderService orderService,
            UserService userService
    ) {
        this.disputeService = disputeService;
        this.orderService = orderService;
        this.userService = userService;
    }

    // ✅ Raise a dispute for an order
    @PostMapping("/raise")
    public ResponseEntity<Dispute> raiseDispute(
            @RequestParam Long orderId,
            @RequestParam Long raisedById,
            @RequestParam String reason
    ) {
        Dispute dispute = disputeService.raiseDispute(orderId, raisedById, reason);
        return ResponseEntity.ok(dispute);
    }

    // ✅ Get disputes for a specific order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Dispute>> getDisputesByOrder(@PathVariable Long orderId) {
        List<Dispute> disputes = disputeService.getDisputesByOrder(orderId);
        return ResponseEntity.ok(disputes);
    }

    // ✅ Get disputes raised by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Dispute>> getDisputesByUser(@PathVariable Long userId) {
        List<Dispute> disputes = disputeService.getDisputesByUser(userId);
        return ResponseEntity.ok(disputes);
    }

    // ✅ Resolve dispute (only admin should call this)
    @PutMapping("/resolve/{disputeId}")
    public ResponseEntity<Dispute> resolveDispute(
            @PathVariable Long disputeId,
            @RequestParam String resolution,
            @RequestParam(required = false) String adminNotes
    ) {
        Dispute dispute = disputeService.resolveDispute(disputeId, resolution, adminNotes);
        return ResponseEntity.ok(dispute);
    }
}
