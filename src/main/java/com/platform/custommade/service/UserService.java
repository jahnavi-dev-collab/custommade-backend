package com.platform.custommade.service;

import com.platform.custommade.exception.UserAlreadyExistsException;
import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // ✅ Constructor Injection (BEST PRACTICE)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ Create new user
    public User createUser(User user) {

        // 🔍 Check email uniqueness
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        // 🔍 Check phone uniqueness
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }

        // ⚙️ Default values
        user.setRole(Role.CUSTOMER);
        user.setActive(true);

        // 💾 Save to database
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
}
