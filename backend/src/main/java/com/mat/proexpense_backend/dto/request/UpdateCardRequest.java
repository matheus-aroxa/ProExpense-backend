package com.mat.proexpense_backend.dto.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mat.proexpense_backend.enums.CardPaymentMethod;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateCardRequest(
  @NotBlank(message = "Name is required")
  @Size(max = 255, message = "Name must be at most 255 characters")
  String name,

  @NotNull(message = "Payment method is required")
  CardPaymentMethod paymentMethod,

  @PositiveOrZero(message = "Limit cannot be negative")
  @DecimalMax(value = "9999999999.99", message = "Maximum limit is 9,999,999,999.99")
  @Digits(integer = 10, fraction = 2, message = "Limit must have at most 10 integer digits and 2 decimal places")
  BigDecimal limit,

  @NotNull(message = "Card status is required")
  @JsonProperty("isActive")
  Boolean isActive
) {}
