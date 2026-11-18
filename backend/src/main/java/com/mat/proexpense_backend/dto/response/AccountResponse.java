package com.mat.proexpense_backend.dto.response;

import java.util.UUID;

import com.mat.proexpense_backend.entitties.Account;

public record AccountResponse(
  UUID id,
  String name,
  UUID ownerId
) {

  public static AccountResponse from(Account account) {
    return new AccountResponse(account.getId(), account.getName(), account.getOwner().getId());
  }
}
