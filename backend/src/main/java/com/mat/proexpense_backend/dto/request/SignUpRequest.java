package com.mat.proexpense_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
  @NotBlank(message = "Name is required")
  @Size(max = 255, message = "Name must be at most 255 characters")
  String name,

  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  @Size(max = 255, message = "Email must be at most 255 characters")
  String email,

  @NotBlank(message = "Password is required")
  @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
  @Pattern(
    regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s]).*$",
    message = "Password must contain letters, numbers and special characters"
  )
  String password
) {}
