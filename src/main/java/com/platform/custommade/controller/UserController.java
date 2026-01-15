package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateUserRequest;
import com.platform.custommade.dto.response.UserResponse;
import com.platform.custommade.model.User;
import com.platform.custommade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 🔍 Health check
    @GetMapping("/health")
    public String health() {
        return "UserController is working!";
    }

    // 📌 Register user (VALIDATED)
    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody CreateUserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        User savedUser = userService.createUser(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }
}
