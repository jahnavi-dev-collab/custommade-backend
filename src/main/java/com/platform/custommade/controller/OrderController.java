package com.platform.custommade.controller;

import com.platform.custommade.model.Order;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ Create order after quote acceptance
    @PostMapping
    public Order createOrder(@RequestParam Long quoteId) {
        return orderService.createOrder(quoteId);
    }

    // ✅ Update order status
    @PutMapping("/{orderId}/status")
    public Order updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        return orderService.updateOrderStatus(orderId, status);
    }
}
