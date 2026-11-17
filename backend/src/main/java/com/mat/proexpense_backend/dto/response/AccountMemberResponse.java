package com.mat.proexpense_backend.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mat.proexpense_backend.entitties.AccountMember;

public record AccountMemberResponse(
  UUID userId,
  String name,
  @JsonProperty("isActive")
  boolean isActive
) {

  public static AccountMemberResponse from(AccountMember member) {
    return new AccountMemberResponse(member.getUser().getId(), member.getUser().getName(), member.isActive());
  }
}
