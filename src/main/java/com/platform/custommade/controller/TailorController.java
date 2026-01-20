package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateQuoteDTO;
import com.platform.custommade.dto.request.CreateUserRequest;
import com.platform.custommade.dto.response.QuoteResponseDTO;
import com.platform.custommade.dto.response.UserResponseDTO;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.service.QuoteService;
import com.platform.custommade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tailor")
public class TailorController {

    private final UserService userService;
    private final QuoteService quoteService;

    public TailorController(UserService userService,
                            QuoteService quoteService) {
        this.userService = userService;
        this.quoteService = quoteService;
    }

    @PostMapping("/register")
    public UserResponseDTO registerTailor(@Valid @RequestBody CreateUserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setGovProofNumber(request.getGovProofNumber());

        // Tailor self register
        User savedUser = userService.createUser(user, Role.TAILOR);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }

    @GetMapping("/orders")
    public String tailorOrders() {
        return "Tailor orders endpoint (ROLE_TAILOR)";
    }
    @PostMapping("/quotes")
    public QuoteResponseDTO createQuote(
            Authentication authentication,
            @RequestBody CreateQuoteDTO dto
    ) {
        String email = authentication.getName();
        User tailor = userService.getCurrentUser(email);

        return quoteService.createQuote(dto, tailor.getId());
    }

    @GetMapping("/quotes")
    public List<QuoteResponseDTO> getMyQuotes(Authentication authentication) {
        String email = authentication.getName();
        User tailor = userService.getCurrentUser(email);

        return quoteService.getQuotesByTailor(tailor.getId());
    }
}
