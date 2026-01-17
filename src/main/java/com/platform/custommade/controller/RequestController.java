package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateRequestDTO;
import com.platform.custommade.dto.response.RequestResponseDTO;
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

    // ✅ Create request
    @PostMapping
    public RequestResponseDTO createRequest(
            @RequestParam("customerId") Long customerId,
            @RequestBody CreateRequestDTO dto
    ) {
        return requestService.createRequest(customerId, dto);
    }

    // ✅ Get all requests of a customer
    @GetMapping("/customer/{customerId}")
    public List<RequestResponseDTO> getRequestsByCustomer(
            @PathVariable("customerId") Long customerId
    ) {
        return requestService.getRequestsByCustomer(customerId);
    }

    // ✅ Get request by ID
    @GetMapping("/{requestId}")
    public RequestResponseDTO getRequestById(
            @PathVariable("requestId") Long requestId
    ) {
        return requestService.getRequestById(requestId);
    }
}
