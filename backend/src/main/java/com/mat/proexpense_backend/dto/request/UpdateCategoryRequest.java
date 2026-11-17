package com.mat.proexpense_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
  @NotBlank(message = "O nome é obrigatório")
  @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
  String name,

  @NotNull(message = "O status da categoria é obrigatório")
  @JsonProperty("isActive")
  Boolean isActive
) {}
