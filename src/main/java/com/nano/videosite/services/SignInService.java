package com.nano.videosite.services;



import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.User;
import com.nano.videosite.repositories.UserRepository;


@Service
public class SignInService {
	@Autowired
	UserRepository repository;
	@Autowired
	JWTAuthenticationService jwtService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
    
	public User signIn(String username, String password, HttpServletResponse res){
		//https://blog.restcase.com/rest-api-error-codes-101/
		
		User user = repository.findByUsername(username).orElseThrow(()->new ElementNotFoundException());
		
		boolean matches = this.passwordEncoder.matches(password, user.getPassword());
		
		if(!matches) {
			System.out.println("credentials error");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return user;
		}
		
		if(matches) {
			jwtService.setAuthentication(user.getId(), username, password,res);
		}
		user.setPassword(null);
		return user;
	}
}
