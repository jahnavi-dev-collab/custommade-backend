package com.platform.custommade.service;

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

    // Create a new quote
    public Quote createQuote(Long requestId, Long tailorId, Quote quote) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        User tailor = userRepository.findById(tailorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tailor not found with id: " + tailorId));

        quote.setRequest(request);
        quote.setTailor(tailor);
        quote.setStatus(QuoteStatus.SUBMITTED);
        quote.setCreatedAt(LocalDateTime.now());

        return quoteRepository.save(quote);
    }

    // Get all quotes for a request
    public List<Quote> getQuotesByRequest(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));

        return quoteRepository.findByRequestId(request.getId());
    }

    // Accept a quote
    public Quote acceptQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        quote.setStatus(QuoteStatus.ACCEPTED);
        return quoteRepository.save(quote);
    }

    // Reject a quote
    public Quote rejectQuote(Long quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote not found with id: " + quoteId));

        quote.setStatus(QuoteStatus.REJECTED);
        return quoteRepository.save(quote);
    }
}
