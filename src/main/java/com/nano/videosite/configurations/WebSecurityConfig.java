package com.nano.videosite.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nano.videosite.filters.JWTAuthenticationFilter;
import com.nano.videosite.filters.corsFilter;
import com.nano.videosite.repositories.UserRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	public UserRepository repository;
	WebSecurityConfig(UserRepository repository){
		this.repository = repository;
	}
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//.csrf().disable()
        http.csrf().disable()
        		.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll() 
//                .antMatchers(HttpMethod.GET, "/login").permitAll() // For Test on Browser
                .antMatchers(HttpMethod.POST, "/register").permitAll()
//                .antMatchers("/playlist/*/*").permitAll() //For Test
//                .antMatchers("/addViewCount").permitAll() //For Test
//                .anyRequest().permitAll() // this line is for test.
//                .anyRequest().authenticated()
                .and().headers().frameOptions().disable()
                .and()
                .addFilterBefore(new corsFilter(), ChannelProcessingFilter.class);
//                .addFilterBefore(new JWTAuthenticationFilter(repository), UsernamePasswordAuthenticationFilter.class);

    }
}
