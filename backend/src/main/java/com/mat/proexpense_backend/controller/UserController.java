package com.mat.proexpense_backend.controller;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mat.proexpense_backend.dto.response.UserResponse;
import com.mat.proexpense_backend.service.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findUserById(@PathVariable UUID id) {
    logger.info("find user by id: {}", id);
    return ResponseEntity.ok(this.service.findUserById(id));
  }

  @GetMapping
  public ResponseEntity<List<UserResponse>> findAllUsers() {
    logger.info("finding all users");
    return ResponseEntity.ok(this.service.findAllUsers());
  }
}
