package com.mat.proexpense_backend.service;

import java.util.Collections;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.mat.proexpense_backend.entitties.User;
import com.mat.proexpense_backend.repository.UserRepository;

@Component 
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository repository;

  public CustomUserDetailsService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.repository.findUserByEmail(username)
                  .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

    return new org.springframework.security.core.userdetails.User(
      user.getEmail(),
      user.getPassword(),
      Collections.emptyList()
    );
  }
  
}
