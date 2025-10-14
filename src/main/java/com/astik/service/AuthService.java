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
import com.astik.exception.RegistrationException;
import com.astik.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            if (request == null) {
                throw new RegistrationException("Registration request cannot be null");
            }

            if (userRepository.existsByUserId(request.getUserId())) {
                throw new RegistrationException("UserId already exists");
            }

            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new RegistrationException("Password cannot be empty");
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

            // First save (persist user + account)
            userRepository.save(user);

            // Generate account number after persistence
            account.setAccountNumber(generateAccountNumber(account.getId()));
            userRepository.save(user);

            RegisterResponse response = new RegisterResponse();
            response.setUserId(user.getUserId());
            response.setFullName(user.getFullName());
            response.setRole(user.getRole());
            response.setAccount(user.getAccount());

            return response;

        } catch (RegistrationException e) {
            throw e; // known validation issues
        } catch (Exception e) {
            throw new RegistrationException("Registration failed due to an internal error", e);
        }
    }

    private String generateAccountNumber(Long id) {
        if (id == null) {
            throw new RegistrationException("Failed to generate account number: Account ID is null");
        }
        return String.format("AC%06d", id);
    }
}
