package com.astik.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.astik.entity.User;
import com.astik.repository.UserRepository;


@Service
public class AdminService {
	 private final UserRepository userRepository;
	 public AdminService(UserRepository userRepository) {
		 this.userRepository = userRepository;
	 }
	 public List<User> getAllUsers() {
		 return userRepository.findAll();
	 }
}
