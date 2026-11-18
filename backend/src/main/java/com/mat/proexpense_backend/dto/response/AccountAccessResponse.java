package com.mat.proexpense_backend.dto.response;

import java.util.UUID;

public record AccountAccessResponse(
  UUID userId,
  UUID accountId,
  boolean hasAccess
) {}
