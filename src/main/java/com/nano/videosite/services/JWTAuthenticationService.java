package com.nano.videosite.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nano.videosite.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTAuthenticationService {
	@Autowired
	private UserRepository repository;
	
    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "Fjapsijf0183lFlso0slfs";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";
    
	public boolean isAuthenticated(String auth) {
        String username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(auth.replace(TOKEN_PREFIX, "")).getBody()
                    .getSubject();
        Authentication a = (username != null) ? new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()) : null;
        boolean exists = repository.existsByUsername(a.getName());
	       System.out.println("does username exist:"+exists);
        if(exists) {
    	    SecurityContextHolder.getContext().setAuthentication(a);
    	    return true;
        }else {
        	return false;
        }
	}
	
	public String setAuthentication(Long userId,String username, String password) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
		authentication.isAuthenticated();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
    	Claims userClaim = Jwts.claims();
    	userClaim.put("usr_id", userId);
    	userClaim.put("sub", username);
        String JWT = Jwts.builder()
        		.setClaims(userClaim)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
		return JWT;
	}
}
