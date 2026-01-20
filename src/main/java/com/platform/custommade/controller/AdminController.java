package com.platform.custommade.controller;

import com.platform.custommade.dto.request.CreateUserRequest;
import com.platform.custommade.dto.response.UserResponseDTO;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.ReviewRepository;
import com.platform.custommade.service.ReviewService;
import com.platform.custommade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    public AdminController(UserService userService,
                           ReviewService reviewService,
                           ReviewRepository reviewRepository) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    // ADMIN ONLY: create admin
    @PostMapping("/create-admin")
    public UserResponseDTO createAdmin(@Valid @RequestBody CreateUserRequest request) {

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        // IMPORTANT: ADMIN should only create ADMIN
        User savedUser = userService.createUser(user, Role.ADMIN);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }

    // ADMIN ONLY: get all users
    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return users.stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getPhone(),
                        u.getRole()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/tailors")
    public List<UserResponseDTO> getAllTailors() {
        return userService.getUsersByRole(Role.TAILOR)
                .stream()
                .map(this::mapToTailorDTO)
                .toList();
    }

    private UserResponseDTO mapToTailorDTO(User user) {

        double avgRating = reviewService.calculateAverageRating(user.getId());
        long reviewCount = reviewRepository.findByTailorId(user.getId()).size();

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                avgRating,
                reviewCount
        );
    }
}
