package com.nano.videosite.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nano.videosite.configurations.MyUserPrincipal;
import com.nano.videosite.models.User;
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

	
	public void setAuthentication(Long userId,String username, String password, HttpServletResponse res) {
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
        res.addHeader("Authorization", JWT);
		return;
	}
	
	public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        System.out.println(token);
        if (token != null) {
            String username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
                    .getSubject();
            Integer userId = (Integer) Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().get("usr_id");
            User user = new User((long) userId,username);
            MyUserPrincipal principal = new MyUserPrincipal(user);
            return username != null ? new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList()) : null;
        }
        return null;
    } 
	
	public static String createToken(String username, Long userId) {
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
