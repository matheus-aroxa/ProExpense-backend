package com.mat.proexpense_backend.dto.response;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mat.proexpense_backend.entitties.Account;
import com.mat.proexpense_backend.entitties.User;

public record AccountResponse(
  UUID id,
  String name,
  UUID ownerId,
  Set<UUID> userIds
) {

  public static AccountResponse from(Account account) {
    Set<UUID> userIds = account.getUsers().stream()
        .map(User::getId)
        .collect(Collectors.toSet());

    return new AccountResponse(account.getId(), account.getName(), account.getOwner().getId(), userIds);
  }
}
