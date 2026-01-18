package com.platform.custommade.controller;

import com.platform.custommade.dto.response.OrderResponseDTO;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order from ACCEPTED Quote
    @PostMapping
    public OrderResponseDTO createOrder(@RequestParam("quoteId") Long quoteId) {
        return orderService.createOrder(quoteId);
    }

    // Get Order by Quote ID
    @GetMapping("/by-quote/{quoteId}")
    public OrderResponseDTO getOrderByQuote(@PathVariable("quoteId") Long quoteId) {
        return orderService.getOrderByQuoteId(quoteId);
    }

    // Get all orders for a customer
    @GetMapping("/customer/{customerId}")
    public List<OrderResponseDTO> getOrdersByCustomer(@PathVariable("customerId") Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    // Get all orders for a tailor
    @GetMapping("/tailor/{tailorId}")
    public List<OrderResponseDTO> getOrdersByTailor(@PathVariable("tailorId") Long tailorId) {
        return orderService.getOrdersByTailor(tailorId);
    }

    // Update Order Status (optional)
    @PutMapping("/{orderId}/status")
    public OrderResponseDTO updateOrderStatus(@PathVariable("orderId") Long orderId,
                                              @RequestParam("status") OrderStatus status) {
        throw new UnsupportedOperationException("Status update not implemented yet");
    }
}
