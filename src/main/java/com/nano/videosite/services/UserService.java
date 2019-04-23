package com.nano.videosite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.User;
import com.nano.videosite.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public User getUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new ElementNotFoundException());
		user.setPassword(null);
		user.setToken(null);;
		return user;
	}
}
