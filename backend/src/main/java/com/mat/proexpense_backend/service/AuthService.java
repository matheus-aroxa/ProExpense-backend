package com.mat.proexpense_backend.service;

import java.util.Locale;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.configuration.JwtUtil;
import com.mat.proexpense_backend.dto.request.SignInRequest;
import com.mat.proexpense_backend.dto.request.SignUpRequest;
import com.mat.proexpense_backend.dto.response.SignInResponse;
import com.mat.proexpense_backend.dto.response.UserResponse;
import com.mat.proexpense_backend.entitties.User;
import com.mat.proexpense_backend.repository.UserRepository;

@Service
public class AuthService {

  private static final String EMAIL_ALREADY_IN_USE = "Email already registered";

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;
  private final JwtUtil jwtUtil;

  public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
      PasswordEncoder encoder, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtUtil = jwtUtil;
  }

  public UserResponse signUp(SignUpRequest request) {
    String email = normalizeEmail(request.email());

    if (this.userRepository.existsByEmail(email)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, EMAIL_ALREADY_IN_USE);
    }

    User user = new User();
    user.setName(request.name());
    user.setEmail(email);
    user.setPassword(this.encoder.encode(request.password()));

    try {
      return UserResponse.from(this.userRepository.saveAndFlush(user));
    } catch (DataIntegrityViolationException e) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, EMAIL_ALREADY_IN_USE);
    }
  }

  public SignInResponse signIn(SignInRequest request) {
    Authentication authentication = this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(normalizeEmail(request.email()), request.password()));

    return SignInResponse.bearer(this.jwtUtil.generateToken(authentication.getName()));
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
}
