package com.platform.custommade.config;

import com.platform.custommade.model.Role;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("superadmin@test.com").isEmpty()) {
            User u = new User();
            u.setName("Super Admin");
            u.setEmail("superadmin@test.com");
            u.setPhone("9999999999");
            u.setRole(Role.ADMIN);
            u.setActive(true);
            u.setPassword(passwordEncoder.encode("admin123"));
            userRepository.save(u);
        }
    }
}
