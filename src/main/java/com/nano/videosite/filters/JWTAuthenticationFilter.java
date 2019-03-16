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
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.services.TokenAuthenticationService;

public class JWTAuthenticationFilter implements Filter{
//	@Autowired
	private UserRepository repository;
	
	public JWTAuthenticationFilter(UserRepository repository){
		this.repository = repository;
	}

	private static final List<String> urlsNotRequiringAuth = new ArrayList<String>(
			Arrays.asList("/login","/register","/h2-console")
			);
	
	private static boolean checkAuthIsNotRequired(String uri) {
		return urlsNotRequiringAuth.stream().anyMatch(uri::equals);
	}
	
   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
           throws IOException, ServletException {

       System.out.println("JWTAuthenticationFilter.doFilter");
       HttpServletRequest req = (HttpServletRequest) servletRequest;
       String uri = req.getRequestURI().toString();
       System.out.println(uri);
       HttpServletResponse res = (HttpServletResponse) servletResponse;

       if(!checkAuthIsNotRequired(uri)) {
    	   System.out.println("going through auth");
	       Authentication authentication = TokenAuthenticationService
	               .getAuthentication((HttpServletRequest) servletRequest);
	       //authentication here is a 'hollow' usernamepasswordtoken with just the username set as authentication.
	       System.out.println("JWTAuthenticationFilter.doFilter repository search");
	       boolean exists = repository.existsByUsername(authentication.getName());
	       System.out.println("does username exist:"+exists);
	       if(exists) {
	    	   SecurityContextHolder.getContext().setAuthentication(authentication);
	       }
       }
       System.out.println(res.getStatus());
       filterChain.doFilter(servletRequest, servletResponse);
   }
   
   @Override
   public void init(FilterConfig filterConfig) {
   }

   @Override
   public void destroy() {
   }
}
