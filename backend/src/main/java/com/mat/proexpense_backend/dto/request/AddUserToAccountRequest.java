package com.mat.proexpense_backend.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AddUserToAccountRequest(
  @NotNull(message = "User is required")
  UUID userId,

  @NotNull(message = "Account is required")
  UUID accountId
) {}
