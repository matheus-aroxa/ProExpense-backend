package com.mat.proexpense_backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mat.proexpense_backend.service.CustomUserDetailsService;

@Configuration 
public class WebSecurityConfig {
  
  private CustomUserDetailsService customUserDetailsService;
  private AuthTokenFilter authTokenFilter;

  public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, AuthTokenFilter authTokenFilter) {
    this.customUserDetailsService = customUserDetailsService;
    this.authTokenFilter = authTokenFilter;
  }

  @Bean 
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder PasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean 
  public SecurityFilterChain SecurityFilterChain(HttpSecurity http) {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> cors.disable())
      .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(authorizeHttpRequests ->
        authorizeHttpRequests
          .requestMatchers("/api/v1/auth/**").permitAll()
          .requestMatchers("/api/v1/ping").permitAll()
          .anyRequest().authenticated()
      );

    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
