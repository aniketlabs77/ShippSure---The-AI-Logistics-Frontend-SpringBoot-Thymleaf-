package com.aniket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	// Authorization
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http
		.authorizeHttpRequests(request -> request.anyRequest().permitAll())

				.oauth2Login(oauth -> oauth.loginPage("/login").defaultSuccessUrl("/customer/google-success", true));

		return http.build();
	}

	// Authentication
	@Bean
	public InMemoryUserDetailsManager userdetailsManager() {
		return new InMemoryUserDetailsManager();
	}

}
