package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateUserRequest;
import com.platform.custommade.dto.response.UserResponse;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.service.UserService;
import jakarta.validation.Valid;
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

    // ✅ New: Get users by role
    // ✅ Get users by role
    @GetMapping
    public List<UserResponse> getUsersByRole(@RequestParam("role") String roleStr) {
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase()); // Convert string to enum safely
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }

        List<User> users = userService.getUsersByRole(role);
        return users.stream()
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getRole()
                ))
                .collect(Collectors.toList());
    }
}
