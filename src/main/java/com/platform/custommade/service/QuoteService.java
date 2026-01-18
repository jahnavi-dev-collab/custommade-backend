package com.platform.custommade.service;

import com.platform.custommade.dto.request.CreateQuoteDTO;
import com.platform.custommade.dto.response.QuoteResponseDTO;
import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.*;
import com.platform.custommade.repository.OrderRepository;
import com.platform.custommade.repository.QuoteRepository;
import com.platform.custommade.repository.RequestRepository;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public QuoteService(QuoteRepository quoteRepository,
                        RequestRepository requestRepository,
                        UserRepository userRepository,
                        OrderRepository orderRepository) {
        this.quoteRepository = quoteRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // ================= CREATE =================
    public QuoteResponseDTO createQuote(CreateQuoteDTO dto, Long tailorId) {

        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + dto.getRequestId()));

        User tailor = userRepository.findById(tailorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tailor not found with id: " + tailorId));

        Quote quote = new Quote();
        quote.setRequest(request);
        quote.setTailor(tailor);
        quote.setPrice(dto.getPrice());
        quote.setDeliveryDays(dto.getDeliveryDays());
        quote.setNotes(dto.getNotes());
        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setCreatedAt(LocalDateTime.now());

        Quote saved = quoteRepository.save(quote);
        return mapToResponseDTO(saved);
    }

    // ================= GET =================
    public QuoteResponseDTO getQuoteById(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));
        return mapToResponseDTO(quote);
    }

    public List<QuoteResponseDTO> getQuotesByRequest(Long requestId) {
        List<Quote> quotes = quoteRepository.findByRequestId(requestId);
        return quotes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ================= ACCEPT =================
    public QuoteResponseDTO acceptQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        // if order already exists for this quote -> just return
        boolean orderExists = orderRepository.existsByQuoteId(quoteId);
        if (orderExists) {
            return mapToResponseDTO(quote);
        }

        quote.setStatus(QuoteStatus.ACCEPTED);
        quoteRepository.save(quote);

        // Update request status
        Request request = quote.getRequest();
        request.setStatus(RequestStatus.ORDERED);
        requestRepository.save(request);

        // Create order
        Order order = new Order();
        order.setRequest(request);
        order.setQuote(quote);
        order.setCustomer(request.getCustomer());
        order.setTailor(quote.getTailor());
        order.setAmount(quote.getPrice());
        order.setStatus(OrderStatus.OPEN);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return mapToResponseDTO(quote);
    }

    // ================= REJECT =================
    public QuoteResponseDTO rejectQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        quote.setStatus(QuoteStatus.REJECTED);
        Quote saved = quoteRepository.save(quote);
        return mapToResponseDTO(saved);
    }

    // ================= MAPPER =================
    private QuoteResponseDTO mapToResponseDTO(Quote quote) {
        QuoteResponseDTO dto = new QuoteResponseDTO();
        dto.setId(quote.getId());
        dto.setRequestId(quote.getRequest().getId());
        dto.setTailorId(quote.getTailor().getId());
        dto.setPrice(quote.getPrice());
        dto.setDeliveryDays(quote.getDeliveryDays());
        dto.setNotes(quote.getNotes());
        dto.setStatus(quote.getStatus().name());
        return dto;
    }
}
