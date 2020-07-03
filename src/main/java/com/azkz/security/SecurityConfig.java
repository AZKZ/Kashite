package com.azkz.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/css/**","/image/**","/js/**").permitAll()
				.mvcMatchers("/").permitAll()
				.anyRequest().authenticated()
				.and()
				.oauth2Login()
				.successHandler(successHandler());
	}

	@Bean
	SimpleUrlAuthenticationSuccessHandler successHandler() {
		return new SimpleUrlAuthenticationSuccessHandler("/login_success");
	}

}
