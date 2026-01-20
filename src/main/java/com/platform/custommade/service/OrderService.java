package com.platform.custommade.service;

import com.platform.custommade.dto.response.OrderResponseDTO;
import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.exception.ConflictException;
import com.platform.custommade.model.*;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.QuoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final QuoteRepository quoteRepository;

    public OrderService(OrderRepository orderRepository,
                        QuoteRepository quoteRepository) {
        this.orderRepository = orderRepository;
        this.quoteRepository = quoteRepository;
    }

    // Create order from accepted quote
    public OrderResponseDTO createOrder(Long quoteId) {

        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ConflictException("Quote not found"));

        // ensure quote is accepted
        if (!quote.getStatus().name().equals("ACCEPTED")) {
            throw new ConflictException("Quote must be ACCEPTED to create order");
        }

        orderRepository.findByQuoteId(quoteId).ifPresent(o -> {
            throw new ConflictException("Order already exists for this quote");
        });

        // If order exists, return existing order
        Optional<Order> existing = orderRepository.findByQuoteId(quoteId);
        if (existing.isPresent()) {
            return mapToResponse(existing.get());
        }

        Order order = new Order();
        order.setQuote(quote);
        order.setRequest(quote.getRequest());
        order.setCustomer(quote.getRequest().getCustomer());
        order.setTailor(quote.getTailor());
        order.setAmount(quote.getPrice());
        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId, String customerEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId)
                );

        // 🔐 Ensure only the customer who owns the order can cancel
        if (!order.getCustomer().getEmail().equals(customerEmail)) {
            throw new ConflictException("You are not allowed to cancel this order");
        }

        //  Only OPEN orders can be cancelled
        if (order.getStatus() != OrderStatus.OPEN) {
            throw new ConflictException("Only OPEN orders can be cancelled");
        }

        // Update order status
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Reset request
        Request request = order.getRequest();
        request.setStatus(RequestStatus.OPEN);

        // Reset accepted quote
        Quote quote = order.getQuote();
        quote.setStatus(QuoteStatus.REJECTED);

        // Persist changes
        quoteRepository.save(quote);

        return mapToResponse(order);
    }

    // Get order by quoteId
    public OrderResponseDTO getOrderByQuoteId(Long quoteId) {

        Order order = orderRepository.findByQuoteId(quoteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found for quoteId: " + quoteId)
                );

        return mapToResponse(order);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus, String actorEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus current = order.getStatus();

        if (current == OrderStatus.CANCELLED) {
            throw new ConflictException("Cancelled order cannot be updated");
        }

        if (newStatus == OrderStatus.IN_PROGRESS) {
            // only tailor can start work
            if (!order.getTailor().getEmail().equals(actorEmail)) {
                throw new ConflictException("Only assigned tailor can start order");
            }
            if (current != OrderStatus.OPEN) {
                throw new ConflictException("Order must be OPEN to start");
            }
        }

        if (newStatus == OrderStatus.COMPLETED) {
            throw new ConflictException("Use delivery confirmation to complete order");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        return mapToResponse(order);
    }

    @Transactional
    public OrderResponseDTO startOrder(Long orderId, String tailorEmail) {
        return updateOrderStatus(orderId, OrderStatus.IN_PROGRESS, tailorEmail);
    }

    @Transactional
    public OrderResponseDTO completeOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new ConflictException("Order must be IN_PROGRESS to complete");
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return mapToResponse(order);
    }

    @Transactional
    public OrderResponseDTO confirmDelivery(Long orderId, Long tailorId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getTailor().getId().equals(tailorId)) {
            throw new ConflictException("Only assigned tailor can confirm delivery");
        }

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new ConflictException("Order must be IN_PROGRESS");
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return mapToResponse(order);
    }

    // Orders by customer
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getOrdersByTailor(Long tailorId) {
        return orderRepository.findByTailorId(tailorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // Mapper
    private OrderResponseDTO mapToResponse(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setTailorId(order.getTailor().getId());
        dto.setRequestId(order.getRequest().getId());
        dto.setQuoteId(order.getQuote().getId());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());

        // Add amount
        dto.setAmount(order.getAmount());

        return dto;
    }
}
