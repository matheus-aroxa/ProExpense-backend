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
  @NotBlank(message = "O nome é obrigatório")
  @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
  String name,

  @NotNull(message = "O método de pagamento é obrigatório")
  CardPaymentMethod paymentMethod,

  @PositiveOrZero(message = "O limite não pode ser negativo")
  @DecimalMax(value = "9999999999.99", message = "O limite máximo é 9.999.999.999,99")
  @Digits(integer = 10, fraction = 2, message = "O limite deve ter no máximo 10 dígitos inteiros e 2 casas decimais")
  BigDecimal limit,

  @NotNull(message = "O status do cartão é obrigatório")
  @JsonProperty("isActive")
  Boolean isActive
) {}
