package com.mat.proexpense_backend.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterCategoryRequest(
  @NotBlank(message = "Name is required")
  @Size(max = 255, message = "Name must be at most 255 characters")
  String name,

  @NotNull(message = "Account id is required")
  UUID accountId
) {}
