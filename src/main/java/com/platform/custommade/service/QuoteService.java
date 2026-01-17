package com.platform.custommade.service;

import com.platform.custommade.dto.request.CreateQuoteDTO;
import com.platform.custommade.dto.response.QuoteResponseDTO;
import com.platform.custommade.exception.ResourceNotFoundException;
import com.platform.custommade.model.Quote;
import com.platform.custommade.model.QuoteStatus;
import com.platform.custommade.model.Request;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.QuoteRepository;
import com.platform.custommade.repository.RequestRepository;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public QuoteService(
            QuoteRepository quoteRepository,
            RequestRepository requestRepository,
            UserRepository userRepository,
            OrderService orderService
    ) {
        this.quoteRepository = quoteRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    // ✅ CREATE QUOTE
    public QuoteResponseDTO createQuote(CreateQuoteDTO dto, Long tailorId) {

        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        User tailor = userRepository.findById(tailorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tailor not found"));

        Quote quote = new Quote();
        quote.setRequest(request);
        quote.setTailor(tailor);
        quote.setPrice(dto.getPrice());
        quote.setDeliveryDays(dto.getDeliveryDays());
        quote.setNotes(dto.getNotes());
        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setCreatedAt(LocalDateTime.now());

        return mapToResponse(quoteRepository.save(quote));
    }

    public List<QuoteResponseDTO> getQuotesByRequest(Long requestId) {
        return quoteRepository.findByRequestId(requestId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET QUOTE BY ID
    public QuoteResponseDTO getQuoteById(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found"));
        return mapToResponse(quote);
    }

    // ✅ ACCEPT QUOTE → CREATE ORDER
    public QuoteResponseDTO acceptQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found"));

        quote.setStatus(QuoteStatus.ACCEPTED);
        quoteRepository.save(quote);

        orderService.createOrder(quoteId);

        return mapToResponse(quote);
    }

    // ✅ REJECT QUOTE
    public QuoteResponseDTO rejectQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found"));

        quote.setStatus(QuoteStatus.REJECTED);
        return mapToResponse(quoteRepository.save(quote));
    }

    // 🔁 MAPPER
    private QuoteResponseDTO mapToResponse(Quote q) {
        QuoteResponseDTO dto = new QuoteResponseDTO();
        dto.setId(q.getId());
        dto.setRequestId(q.getRequest().getId());
        dto.setTailorId(q.getTailor().getId());
        dto.setPrice(q.getPrice());
        dto.setDeliveryDays(q.getDeliveryDays());
        dto.setNotes(q.getNotes());
        dto.setStatus(q.getStatus().name());
        return dto;
    }
}
