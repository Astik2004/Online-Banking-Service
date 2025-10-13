package com.astik.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.astik.dto.AuthRequest;
import com.astik.dto.RegisterRequest;
import com.astik.dto.RegisterResponse;
import com.astik.security.JwtUtils;
import com.astik.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest auth) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getUserId(), auth.getPassword())
            );

            String token = jwtUtils.createToken(auth.getUserId());
            System.out.println(token);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body("Login successful. Welcome " + auth.getUserId());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}
