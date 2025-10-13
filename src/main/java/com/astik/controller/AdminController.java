package com.astik.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.astik.entity.User;
import com.astik.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	 private final AdminService adminService;
	 
	 public AdminController(AdminService adminService) {
		 this.adminService = adminService;
	 }
	 
	 @GetMapping("/users")
	 public ResponseEntity<List<User>> getUsers() 
	 {
		 return ResponseEntity.ok(adminService.getAllUsers ());
	 }
}
