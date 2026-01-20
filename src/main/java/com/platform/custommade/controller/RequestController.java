package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateRequestDTO;
import com.platform.custommade.dto.response.RequestResponseDTO;
import com.platform.custommade.model.User;
import com.platform.custommade.service.RequestService;
import com.platform.custommade.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class RequestController {

    private final RequestService requestService;
    private final UserService userService;

    public RequestController(RequestService requestService,
                             UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping("/requests")
    public RequestResponseDTO createRequest(
            Authentication authentication,
            @RequestBody CreateRequestDTO dto
    ) {
        String email = authentication.getName();
        User customer = userService.getCurrentUser(email);

        return requestService.createRequest(customer.getId(), dto);
    }

    @GetMapping("/requests")
    public List<RequestResponseDTO> getMyRequests(Authentication authentication) {
        String email = authentication.getName();
        return requestService.getRequestsByCustomer(email);
    }
}
