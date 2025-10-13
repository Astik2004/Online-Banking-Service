package com.astik.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.astik.entity.User;
import com.astik.repository.UserRepository;
@Component
public class UDService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
	        Optional<User> userInfo = userRepository.findByUserId(userId);
	        if (userInfo.isPresent()) {
	            return userInfo.get();
	        } else {
	            throw new UsernameNotFoundException("User not found with userId: " + userId);
	        }
	 }
}
