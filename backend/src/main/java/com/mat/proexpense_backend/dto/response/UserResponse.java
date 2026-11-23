package com.mat.proexpense_backend.dto.response;

import java.util.UUID;

import com.mat.proexpense_backend.entitties.User;

public record UserResponse(
  UUID id,
  String name,
  String email
) {

  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail());
  }
}
