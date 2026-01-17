package com.platform.custommade.service;

import com.platform.custommade.dto.response.OrderResponseDTO;
import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.Order;
import com.platform.custommade.model.OrderStatus;
import com.platform.custommade.model.Quote;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    // ✅ Create order from accepted quote
    public OrderResponseDTO createOrder(Long quoteId) {

        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Quote not found with id: " + quoteId)
                );

        Order order = new Order();
        order.setQuote(quote);
        order.setRequest(quote.getRequest());
        order.setCustomer(quote.getRequest().getCustomer());
        order.setTailor(quote.getTailor());
        order.setStatus(OrderStatus.PAID);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    // ✅ Get order by quoteId
    public OrderResponseDTO getOrderByQuoteId(Long quoteId) {

        Order order = orderRepository.findByQuoteId(quoteId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found for quoteId: " + quoteId)
                );

        return mapToResponse(order);
    }

    // ✅ Orders by customer
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Orders by tailor
    public List<OrderResponseDTO> getOrdersByTailor(Long tailorId) {
        return orderRepository.findByTailorId(tailorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔁 MAPPER (THIS WAS MISSING)
    private OrderResponseDTO mapToResponse(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setTailorId(order.getTailor().getId());
        dto.setRequestId(order.getRequest().getId());
        dto.setQuoteId(order.getQuote().getId());
        dto.setStatus(order.getStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }
}
