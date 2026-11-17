package com.mat.proexpense_backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mat.proexpense_backend.entitties.Card;
import com.mat.proexpense_backend.enums.CardPaymentMethod;

public record CardResponse(
  UUID id,
  UUID ownerId,
  UUID accountId,
  String name,
  CardPaymentMethod paymentMethod,
  BigDecimal limit,
  @JsonProperty("isActive")
  boolean isActive
) {

  public static CardResponse from(Card card) {
    return new CardResponse(
        card.getId(),
        card.getOwner().getId(),
        card.getAccount().getId(),
        card.getName(),
        card.getPaymentMethod(),
        card.getLimit(),
        card.isActive());
  }
}
