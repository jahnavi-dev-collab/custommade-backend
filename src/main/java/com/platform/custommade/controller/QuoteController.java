package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateQuoteDTO;
import com.platform.custommade.dto.response.QuoteResponseDTO;
import com.platform.custommade.service.QuoteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    // ✅ CREATE QUOTE (Tailor)
    @PostMapping
    public QuoteResponseDTO createQuote(
            @RequestParam("tailorId") Long tailorId,
            @RequestBody CreateQuoteDTO dto
    ) {
        return quoteService.createQuote(dto, tailorId);
    }

    // ✅ GET QUOTES BY REQUEST
    @GetMapping("/request/{requestId}")
    public List<QuoteResponseDTO> getQuotesByRequest(
            @PathVariable("requestId") Long requestId
    ) {
        return quoteService.getQuotesByRequest(requestId);
    }

    // ✅ GET QUOTE BY ID
    @GetMapping("/{quoteId}")
    public QuoteResponseDTO getQuoteById(@PathVariable Long quoteId) {
        return quoteService.getQuoteById(quoteId);
    }

    // ✅ ACCEPT QUOTE
    @PutMapping("/{quoteId}/accept")
    public QuoteResponseDTO acceptQuote(
            @PathVariable("quoteId") Long quoteId
    ) {
        return quoteService.acceptQuote(quoteId);
    }

    // ✅ REJECT QUOTE
    @PutMapping("/{quoteId}/reject")
    public QuoteResponseDTO rejectQuote(@PathVariable Long quoteId) {
        return quoteService.rejectQuote(quoteId);
    }
}
