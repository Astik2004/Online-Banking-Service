package com.astik.dto;

import com.astik.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	 private String userId;
	 private String fullName;
	 private String password;
}
