package com.mat.proexpense_backend.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AddUserToAccountRequest(
  @NotNull(message = "O usuário é obrigatório")
  UUID userId,

  @NotNull(message = "A conta é obrigatória")
  UUID accountId
) {}
