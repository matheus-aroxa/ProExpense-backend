package com.mat.proexpense_backend.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mat.proexpense_backend.entitties.Category;

public record CategoryResponse(
  UUID id,
  UUID accountId,
  String name,
  @JsonProperty("isActive")
  boolean isActive
) {

  public static CategoryResponse from(Category category) {
    return new CategoryResponse(
        category.getId(),
        category.getAccount().getId(),
        category.getName(),
        category.isActive());
  }
}
