package com.nano.videosite.services;


import java.io.IOException;

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
	JWTAuthenticationService jwtService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	SignUpService(UserRepository repository){
		this.repository = repository;
	}
	
	private String validate(User user) {
		String nameRegex = "^[\\w][^\\s]{1,}$";
		if(!user.getName().matches(nameRegex)) {
			return "Name should be more than 1 character";
		}
		String usernameRegex = "^.[^\\s]{4,24}$";
		if(!user.getUsername().matches(usernameRegex)) {
			return "Username should be between 4 and 24 characters";
		}
		String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		if(!user.getEmail().matches(emailRegex)) {
			return "Email is not in the correct format.";
		}
		String passwordRegex = "^.[^\\s]{6,}$";
		if(!user.getPassword().matches(passwordRegex)) {
			System.out.println(user.getPassword());
			return "Password should be more than 7 characters long.";
		}
		if(repository.existsByUsername(user.getUsername())) {
			return "Username already exists";
		}
		if(repository.existsByEmail(user.getEmail())) {
			return "Email already exists";
		}
		return "true";
	}
    
	public User signup(User user,HttpServletResponse res) throws IOException{
		String encryptedPassword = this.passwordEncoder.encode(user.getPassword());
		String validation = validate(user);
		if(validation == "true") {
			jwtService.setAuthentication(user.getId(), user.getUsername(), user.getPassword(),res);
			user.setPassword(encryptedPassword);
			repository.save(user);
			
		}else {
			res.sendError(409, validation);
			return null;
		}
		user.setPassword(null);
		return user;
	}
}
