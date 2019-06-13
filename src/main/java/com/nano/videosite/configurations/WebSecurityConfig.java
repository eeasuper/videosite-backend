package com.nano.videosite.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nano.videosite.filters.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	
	WebSecurityConfig(){
	}
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        		.cors().and()
        		.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll() 
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/addViewCount").permitAll()
                .antMatchers(HttpMethod.GET, "/getViewCount/*").permitAll()
                .antMatchers(HttpMethod.GET, "/video/**").permitAll()
                .antMatchers(HttpMethod.GET, "/search/*").permitAll()
                .antMatchers(HttpMethod.GET, "/playlist/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/*").permitAll()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .antMatchers(HttpMethod.HEAD).authenticated()
                .antMatchers(HttpMethod.OPTIONS).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.PATCH).authenticated()
                .antMatchers(HttpMethod.TRACE).authenticated()
                .and().headers().frameOptions().disable()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
	private final String frontEndDomain = "https://video-site-frontend.herokuapp.com";
//	private final String frontEndDomain = "http://localhost:4200";
    @Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		List<String> originList = new ArrayList<String>(
				Arrays.asList(frontEndDomain));
		configuration.setAllowedOrigins(originList);
		List<String> list = new ArrayList<String>(
				Arrays.asList("*"));
		configuration.setAllowedMethods(list);
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(list);
		List<String> exposedList = new ArrayList<String>(
				Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "Access-Control-Max-Age",
						"Access-Control-Request-Headers", "Access-Control-Request-Method","Authorization"));
		configuration.setExposedHeaders(exposedList);
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
