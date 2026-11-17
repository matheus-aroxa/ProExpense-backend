package com.mat.proexpense_backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.mat.proexpense_backend.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateExpenseRequest(
  @NotBlank(message = "O nome é obrigatório")
  @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
  String name,

  @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
  String description,

  @NotNull(message = "O valor é obrigatório")
  @Positive(message = "O valor deve ser maior que zero")
  @DecimalMax(value = "9999999999.99", message = "O valor máximo é 9.999.999.999,99")
  @Digits(integer = 10, fraction = 2, message = "O valor deve ter no máximo 10 dígitos inteiros e 2 casas decimais")
  BigDecimal amount,

  @NotNull(message = "A forma de pagamento é obrigatória")
  PaymentMethod paymentMethod,

  @NotNull(message = "A data do gasto é obrigatória")
  LocalDate expenseDate,

  UUID categoryId,

  @NotNull(message = "O cartão é obrigatório")
  UUID cardId
) {}
