package com.astik.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.astik.dto.RegisterRequest;
import com.astik.dto.RegisterResponse;
import com.astik.entity.Account;
import com.astik.entity.Role;
import com.astik.entity.User;
import com.astik.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("UserId already exists");
        }
        
        User user = User.builder()
                .userId(request.getUserId())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .build();

        Account account = Account.builder()
                .balance(0.0)
                .user(user)
                .build();

        user.setAccount(account);

        userRepository.save(user);

        account.setAccountNumber(generateAccountNumber(account.getId()));
        userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setUserId(user.getUserId());
        response.setFullName(user.getFullName());
        response.setPassword(user.getPassword());
        response.setRole(user.getRole());
        response.setAccount(user.getAccount());

        return response;
    }

    private String generateAccountNumber(Long id) {
        return String.format("AC%06d", id);
    }
}
