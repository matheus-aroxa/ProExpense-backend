package com.mat.proexpense_backend.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.mat.proexpense_backend.enums.PaymentMethod;
import com.mat.proexpense_backend.enums.RecurrenceUnit;
import com.mat.proexpense_backend.validation.InstallmentPlan;
import com.mat.proexpense_backend.validation.RecurrencePlan;
import com.mat.proexpense_backend.validation.ValidInstallments;
import com.mat.proexpense_backend.validation.ValidRecurrence;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@ValidInstallments
@ValidRecurrence
public record RegisterExpenseRequest(
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

  @NotNull(message = "O dono da despesa é obrigatório")
  UUID ownerId,

  @NotNull(message = "O id da conta é obrigatório")
  UUID accountId,

  UUID categoryId,

  @NotNull(message = "O cartão é obrigatório")
  UUID cardId,

  boolean isInInstallments,
  int installmentsNumber,

  RecurrenceUnit recurrenceUnit,

  @Positive(message = "O intervalo da recorrência deve ser maior que zero")
  @Max(value = 365, message = "O intervalo da recorrência deve ser no máximo 365")
  Integer recurrenceInterval,

  @Min(value = 1, message = "O dia da recorrência deve estar entre 1 e 31")
  @Max(value = 31, message = "O dia da recorrência deve estar entre 1 e 31")
  Integer recurrenceDayOfMonth
) implements InstallmentPlan, RecurrencePlan {}
