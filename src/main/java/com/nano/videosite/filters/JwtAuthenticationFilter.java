package com.nano.videosite.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.services.JWTAuthenticationService;

@Component
public class JwtAuthenticationFilter implements Filter{
	
	@Autowired
	private UserRepository userRepository;
	
	private static final List<String> urlsNotRequiringAuth = new ArrayList<String>(
		Arrays.asList("\\/login","\\/register","\\/h2-console.*","\\/addViewCount","\\/getViewCount","\\/video\\/[a-z]*[0-9]*\\/?[a-z]*[0-9]*","\\/search\\/([a-z]*[0-9]*)*","\\/playlist\\/([a-z]*[0-9]*)*\\/*","\\/user\\/([a-z]*[0-9]*)*")
	);
	
	private static boolean isAuthNotRequired(String uri) {
		return urlsNotRequiringAuth.stream().anyMatch(uri::matches);
	}
	
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	           throws IOException, ServletException {
	    HttpServletRequest req = (HttpServletRequest) servletRequest;
	    String uri = req.getRequestURI().toString();
	    if(!isAuthNotRequired(uri)|| (req.getMethod()!= "GET" && !uri.matches("\\/addViewCount"))) {
		    Authentication authentication = JWTAuthenticationService
		    		.getAuthentication((HttpServletRequest) servletRequest);
		    if(authentication != null) {
		    	boolean exists = userRepository.existsByUsername(authentication.getName());
		    	System.out.println(exists);
			    if(exists) {
			 	    SecurityContextHolder.getContext().setAuthentication(authentication);
			    }
		    }
	    }
	    filterChain.doFilter(servletRequest, servletResponse);
	   }  
	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
		
	}	
}
