package com.nano.videosite.services;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nano.videosite.models.User;
import com.nano.videosite.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SignInService {
	@Autowired
	UserRepository repository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
  static final long EXPIRATIONTIME = 864_000_000; // 10 days
  
  static final String SECRET = "Fjapsijf0183lFlso0slfs";
   
  static final String TOKEN_PREFIX = "Bearer";
   
  static final String HEADER_STRING = "Authorization";
    
	public User signIn(String username, String password, HttpServletResponse res){
		//https://blog.restcase.com/rest-api-error-codes-101/
		
//		String usernameFromToken = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
//                .getSubject();
		User user = repository.findByUsername(username).orElseThrow();
		
		boolean matches = this.passwordEncoder.matches(password, user.getPassword());
		
		if(!matches) {
			System.out.println("credentials error");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return user;
		}
		
		//does password inside spring token have to be encoded?
		if(matches) {
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
			authentication.isAuthenticated();
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
	    	Claims userClaim = Jwts.claims();
	    	userClaim.put("usr_id", user.getId());
	    	userClaim.put("sub", username);
	        String JWT = Jwts.builder()
	        		.setClaims(userClaim)
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
			user.setToken(JWT);
		}
		user.setPassword(null);
		return user;
	}
}
