package com.nano.videosite.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.User;
import com.nano.videosite.services.SignInService;
import com.nano.videosite.services.SignUpService;
import com.nano.videosite.services.UserService;

@RestController
public class UserController {
	
	@Autowired
	SignUpService signupService;
	@Autowired
	SignInService signinService;
	@Autowired
	UserService userService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/register", produces = {MediaType.APPLICATION_JSON_VALUE})
	ResponseEntity<?> newUser(@RequestBody User newUser) throws URISyntaxException {
		//successful curl request:
		//curl -v localhost:8080/register --header "Accept: application/json" --header "Content-Type: application/json" --data "{\"name\":\"test1\",\"username\":\"testusername1\",\"password\":\"testpassword1\",\"email\":\"testemail1\", \"token\":\"testtoken\"}"
		//request format:
		/*
		 * {
		      username: username,
		      name: name,
		      email: email,
		      password: password
		    }
		 */
		User user = signupService.signup(newUser);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(user);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
	ResponseEntity<?> signIn(@RequestBody User newUser, HttpServletResponse res) throws URISyntaxException{
		//successful curl request:
		//curl -v localhost:8080/login --header "Accept: application/json" --header "Content-Type: application/json" --data "{\"name\":\"name\",\"username\":\"username\",\"password\":\"password\",\"email\":\"email@email.com\", \"token\":\"testtoken\"}"
		//curl -v localhost:8080/login -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZSJ9.xOXxbTzfz7eQKjacwVVkOhT0oodx58GX6Wi7e3aiM0Q" -H "Accept: application/json" --header "Content-Type: application/json" --user username:password --data "{\"name\":\"name\",\"username\":\"username\",\"password\":\"password\",\"email\":\"email@email.com\", \"token\":\"testtoken\"}"
		//https://stackoverflow.com/questions/22453550/custom-authentication-provider-not-being-called/22457561#22457561
		//curl -v localhost:8080/login -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZSJ9.xOXxbTzfz7eQKjacwVVkOhT0oodx58GX6Wi7e3aiM0Q" -H "Accept: application/json" --header "Content-Type: application/x-www-form-urlencoded" --data "username=username&password=password"
		//https://stackoverflow.com/questions/31826233/custom-authentication-manager-with-spring-security-and-java-configuration
		/*
		 * request format:
		 * state = {
		      username: this.state.username,
		      password: this.state.password,
		      stayLoggedIn: this.state.stayLoggedIn
		    }
		 */
		System.out.println("user:"+newUser.getUsername()+" "+ newUser.getPassword());
		User user = signinService.signIn(newUser.getUsername(), newUser.getPassword(), res);
		int status = res.getStatus();
		
		if(status == 201 || status == 200) {
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(user);
		}else {
			return ResponseEntity.status(status).build();
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/user/{id}",  produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
		User user = userService.getUser(id);
		return ResponseEntity.ok(user);
	}
}
