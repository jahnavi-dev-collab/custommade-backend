package com.platform.custommade.controller;

import com.platform.custommade.dto.response.OrderResponseDTO;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.service.OrderService;
import com.platform.custommade.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService,
                           UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // =========================
    // CREATE / FETCH
    // =========================

    // Create Order from ACCEPTED Quote
    @PostMapping
    public OrderResponseDTO createOrder(@RequestParam("quoteId") Long quoteId) {
        return orderService.createOrder(quoteId);
    }

    // Get Order by Quote ID
    @GetMapping("/by-quote/{quoteId}")
    public OrderResponseDTO getOrderByQuote(@PathVariable Long quoteId) {
        return orderService.getOrderByQuoteId(quoteId);
    }

    // =========================
    // CUSTOMER (JWT-based)
    // =========================

    @GetMapping("/customer/orders")
    public List<OrderResponseDTO> getCustomerOrders(Authentication authentication) {
        String email = authentication.getName();
        Long customerId = userService.getCurrentUser(email).getId();
        return orderService.getOrdersByCustomer(customerId);
    }

    // =========================
    // TAILOR (JWT-based)
    // =========================

    @GetMapping("/tailor/orders")
    public List<OrderResponseDTO> getTailorOrders(Authentication authentication) {
        String email = authentication.getName();
        Long tailorId = userService.getCurrentUser(email).getId();
        return orderService.getOrdersByTailor(tailorId);
    }

    // =========================
    // ADMIN LOOKUPS
    // =========================

    @GetMapping("/admin/customer/{customerId}")
    public List<OrderResponseDTO> getOrdersByCustomerAdmin(
            @PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/admin/tailor/{tailorId}")
    public List<OrderResponseDTO> getOrdersByTailorAdmin(
            @PathVariable Long tailorId) {
        return orderService.getOrdersByTailor(tailorId);
    }

    // =========================
    // ORDER LIFECYCLE
    // =========================

    @PutMapping("/{orderId}/start")
    public OrderResponseDTO startOrder(@PathVariable Long orderId,
                                       Authentication authentication) {
        return orderService.startOrder(orderId, authentication.getName());
    }

    @PutMapping("/{orderId}/deliver")
    public OrderResponseDTO confirmDelivery(@PathVariable Long orderId,
                                            Authentication authentication) {
        Long tailorId = userService.getCurrentUser(authentication.getName()).getId();
        return orderService.confirmDelivery(orderId, tailorId);
    }

    @PutMapping("/{orderId}/cancel")
    public OrderResponseDTO cancelOrder(@PathVariable Long orderId,
                                        Authentication authentication) {
        return orderService.cancelOrder(orderId, authentication.getName());
    }

    // =========================
    // OPTIONAL (future)
    // =========================

    @PutMapping("/{orderId}/status")
    public OrderResponseDTO updateOrderStatus(@PathVariable Long orderId,
                                              @RequestParam OrderStatus status) {
        throw new UnsupportedOperationException("Status update not implemented yet");
    }
}
