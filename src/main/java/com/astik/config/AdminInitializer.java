package com.astik.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.astik.entity.Role;
import com.astik.entity.User;
import com.astik.repository.UserRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin already exists
        if (!userRepository.existsByUserId("admin@gmail.com")) {
            User admin = User.builder()
                    .userId("admin@gmail.com")
                    .fullName("System Administrator")
                    .password(passwordEncoder.encode("admin123")) // encoded password
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("Default Admin Created Successfully -> userId: admin@gmail.com | password: admin123");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
