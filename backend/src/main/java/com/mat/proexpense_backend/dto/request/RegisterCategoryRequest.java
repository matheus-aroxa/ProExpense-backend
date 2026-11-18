package com.mat.proexpense_backend.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterCategoryRequest(
  @NotBlank(message = "O nome é obrigatório")
  @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
  String name,

  @NotNull(message = "O id da conta é obrigatório")
  UUID accountId
) {}
