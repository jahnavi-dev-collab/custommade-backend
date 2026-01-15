package com.platform.custommade.controller;

import com.platform.custommade.model.Request;
import com.platform.custommade.service.RequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    // Create request
    @PostMapping
    public Request createRequest(
            @RequestParam("customerId") Long customerId,
            @RequestBody Request request
    ) {
        return requestService.createRequest(customerId, request);
    }

    // Get all requests of a customer
    @GetMapping("/customer/{customerId}")
    public List<Request> getRequestsByCustomer(
            @PathVariable("customerId") Long customerId
    ) {
        return requestService.getRequestsByCustomer(customerId);
    }

    // Get request by ID
    @GetMapping("/{requestId}")
    public Request getRequestById(
            @PathVariable("requestId") Long requestId
    ) {
        return requestService.getRequestById(requestId);
    }
}
