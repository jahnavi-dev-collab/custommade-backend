package com.platform.custommade.service;

import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.model.Quote;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final QuoteRepository quoteRepository;

    // Constructor Injection (best practice)
    public OrderService(OrderRepository orderRepository, QuoteRepository quoteRepository) {
        this.orderRepository = orderRepository;
        this.quoteRepository = quoteRepository;
    }

    // Create an order from a quote
    public Order createOrder(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        Order order = new Order();
        order.setQuote(quote);
        order.setRequest(quote.getRequest());
        order.setCustomer(quote.getRequest().getCustomer());
        order.setTailor(quote.getTailor());
        order.setStatus(OrderStatus.PAID); // initially PAID once advance payment is done
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    // Update order status
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // Get all orders by customer
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    // Get all orders by tailor
    public List<Order> getOrdersByTailor(Long tailorId) {
        return orderRepository.findByTailorId(tailorId);
    }
}
