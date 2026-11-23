package com.mat.proexpense_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.controller.docs.AuthControllerDocs;
import com.mat.proexpense_backend.dto.request.SignInRequest;
import com.mat.proexpense_backend.dto.request.SignUpRequest;
import com.mat.proexpense_backend.dto.response.SignInResponse;
import com.mat.proexpense_backend.dto.response.UserResponse;
import com.mat.proexpense_backend.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
  }

  @Override
  @PostMapping("/signin")
  public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
    logger.info("Trying to sign in");
    return ResponseEntity.ok(this.service.signIn(request));
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
    logger.info("Trying to sign up");
    return ResponseEntity.status(HttpStatus.CREATED).body(this.service.signUp(request));
  }
}
