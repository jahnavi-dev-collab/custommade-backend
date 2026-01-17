package com.platform.custommade.service;

import com.platform.custommade.exception.UserAlreadyExistsException;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Create new user
    public User createUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }

        user.setRole(Role.CUSTOMER); // Default role
        user.setActive(true);

        return userRepository.save(user);
    }

    // 🔎 Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 🔎 Find user by phone
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    // ✅ New: find all users by role
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
}
