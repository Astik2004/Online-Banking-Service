package com.astik.dto;

import com.astik.entity.Account;
import com.astik.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
	private String userId;
    private String fullName;
    private String password;
    private Role role; // ADMIN or CUSTOMER
    private Account account;
}
