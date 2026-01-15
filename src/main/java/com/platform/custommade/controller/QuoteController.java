package com.platform.custommade.controller;

import com.platform.custommade.model.Quote;
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

    // ✅ Tailor creates a quote for a request
    @PostMapping
    public Quote createQuote(
            @RequestParam Long requestId,
            @RequestParam Long tailorId,
            @RequestBody Quote quote
    ) {
        return quoteService.createQuote(requestId, tailorId, quote);
    }

    // ✅ Customer views all quotes for a request
    @GetMapping("/request/{requestId}")
    public List<Quote> getQuotesByRequest(@PathVariable Long requestId) {
        return quoteService.getQuotesByRequest(requestId);
    }

    // ✅ Customer accepts a quote
    @PutMapping("/{quoteId}/accept")
    public Quote acceptQuote(@PathVariable Long quoteId) {
        return quoteService.acceptQuote(quoteId);
    }

    // ✅ Customer rejects a quote
    @PutMapping("/{quoteId}/reject")
    public Quote rejectQuote(@PathVariable Long quoteId) {
        return quoteService.rejectQuote(quoteId);
    }
}
