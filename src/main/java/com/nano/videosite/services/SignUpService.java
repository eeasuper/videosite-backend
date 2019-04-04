package com.nano.videosite.services;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nano.videosite.models.User;
import com.nano.videosite.repositories.UserRepository;

@Service
public class SignUpService {
	@Autowired
	UserRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	SignUpService(UserRepository repository){
		this.repository = repository;
	}
    
	public User signup(User user){
		String encryptedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		repository.save(user);
		user.setPassword(null);
		return user;
	}
}
