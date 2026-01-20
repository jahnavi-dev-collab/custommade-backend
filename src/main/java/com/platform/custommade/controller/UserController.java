package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateUserRequest;
import com.platform.custommade.dto.response.UserResponseDTO;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔍 Health check
    @GetMapping("/health")
    public String health() {
        return "UserController is working!";
    }

    // 📌 Register user
    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody CreateUserRequest request) {

        // 🔍 DEBUG LOG (temporary)
        System.out.println("REGISTER HIT: " + request.getEmail());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        User savedUser = userService.createUser(user, Role.CUSTOMER);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }

    //  New: Get users by role
    //  Get users by role
    //  Get current logged-in user
    @GetMapping("/me")
    public UserResponseDTO getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName(); // JWT subject

        User user = userService.getCurrentUser(email);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
