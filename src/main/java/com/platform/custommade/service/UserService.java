package com.platform.custommade.service;

import com.platform.custommade.exception.UserAlreadyExistsException;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Generic method
    public User createUser(User user, Role role) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }

        if (role == Role.TAILOR && (user.getGovProofNumber() == null || user.getGovProofNumber().isEmpty())) {
            throw new IllegalArgumentException("Tailor must provide government proof number");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role if null
        user.setRole(role == null ? Role.CUSTOMER : role);
        user.setActive(true);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
