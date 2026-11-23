package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.response.UserResponse;
import com.mat.proexpense_backend.entitties.User;
import com.mat.proexpense_backend.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public UserResponse findUserById(UUID id) {
    return UserResponse.from(this.findUser(id));
  }

  public List<UserResponse> findAllUsers() {
    return this.repository.findAll().stream()
        .map(UserResponse::from)
        .toList();
  }

  private User findUser(UUID id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }
}
