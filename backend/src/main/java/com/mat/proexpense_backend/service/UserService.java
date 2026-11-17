package com.mat.proexpense_backend.service;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.mat.proexpense_backend.dto.request.RegisterUserRequest;
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

  public List<UserResponse> findAllActiveUsers() {
    return this.repository.findAllByIsActiveTrue().stream()
        .map(UserResponse::from)
        .toList();
  }

  public void deactivateUser(UUID id) {
    User user = this.findUser(id);
    user.setActive(false);
    this.repository.save(user);
  }

  private User findUser(UUID id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
  }

  public UserResponse createUser(RegisterUserRequest request) {
    User user = new User();
    user.setName(request.name());

    return UserResponse.from(this.repository.save(user));
  }
}
