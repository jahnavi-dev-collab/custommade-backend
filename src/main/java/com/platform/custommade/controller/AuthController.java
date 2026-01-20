package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateUserRequest;
import com.platform.custommade.dto.request.LoginRequest;
import com.platform.custommade.dto.response.UserResponseDTO;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.platform.custommade.security.JwtUtils;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 PASSWORD MATCH
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtils.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
    }

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        User saved = userService.createUser(user, Role.CUSTOMER);

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getRole()
        );
    }

}
