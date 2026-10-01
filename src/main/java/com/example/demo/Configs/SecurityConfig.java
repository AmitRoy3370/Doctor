package com.example.demo.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.support.MultipartFilter;

import com.example.demo.Security.JwtFilter;

@Configuration
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	@Autowired
	private RateLimitingFilter rateLimitingFilter;

	public SecurityConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http

				.csrf(csrf -> csrf.disable())

				.cors(cors -> {
				})

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.authorizeHttpRequests(auth -> auth

						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						.requestMatchers("/api/users/register", "/api/users/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/**").permitAll()

						.anyRequest().authenticated())

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(rateLimitingFilter, JwtFilter.class);

		return http.build();
	}

	@Bean
	public FilterRegistrationBean<MultipartFilter> multipartFilter() {
		FilterRegistrationBean<MultipartFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new MultipartFilter());

		return registrationBean;
	}

}
