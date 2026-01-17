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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public QuoteService(QuoteRepository quoteRepository,
                        RequestRepository requestRepository,
                        UserRepository userRepository) {
        this.quoteRepository = quoteRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    // ================= CREATE =================
    public QuoteResponseDTO createQuote(CreateQuoteDTO dto, Long tailorId) {
        // Check request exists
        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + dto.getRequestId()));

        // Check tailor exists
        User tailor = userRepository.findById(tailorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tailor not found with id: " + tailorId));

        // Create quote
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

    // ================= READ =================
    public List<QuoteResponseDTO> getQuotesByRequest(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        return quoteRepository.findByRequestId(request.getId())
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public QuoteResponseDTO getQuoteById(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        return mapToResponseDTO(quote);
    }

    // ================= UPDATE =================
    public QuoteResponseDTO acceptQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        quote.setStatus(QuoteStatus.ACCEPTED);
        Quote saved = quoteRepository.save(quote);
        return mapToResponseDTO(saved);
    }

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
