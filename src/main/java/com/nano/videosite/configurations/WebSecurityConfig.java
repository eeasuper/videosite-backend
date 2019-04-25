package com.nano.videosite.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import com.nano.videosite.filters.CorsSiteFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	WebSecurityConfig(){
	}
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        		.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll() 
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .and().headers().frameOptions().disable()
                .and()
                .addFilterBefore(new CorsSiteFilter(), ChannelProcessingFilter.class);

    }
}
