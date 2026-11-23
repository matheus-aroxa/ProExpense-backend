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
  @NotBlank(message = "Name is required")
  @Size(max = 255, message = "Name must be at most 255 characters")
  String name,

  @Size(max = 255, message = "Description must be at most 255 characters")
  String description,

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be greater than zero")
  @DecimalMax(value = "9999999999.99", message = "Maximum amount is 9,999,999,999.99")
  @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 integer digits and 2 decimal places")
  BigDecimal amount,

  @NotNull(message = "Payment method is required")
  PaymentMethod paymentMethod,

  @NotNull(message = "Expense date is required")
  LocalDate expenseDate,

  @NotNull(message = "Expense owner is required")
  UUID ownerId,

  @NotNull(message = "Account id is required")
  UUID accountId,

  UUID categoryId,

  @NotNull(message = "Card is required")
  UUID cardId,

  boolean isInInstallments,
  int installmentsNumber,

  RecurrenceUnit recurrenceUnit,

  @Positive(message = "Recurrence interval must be greater than zero")
  @Max(value = 365, message = "Recurrence interval must be at most 365")
  Integer recurrenceInterval,

  @Min(value = 1, message = "Recurrence day must be between 1 and 31")
  @Max(value = 31, message = "Recurrence day must be between 1 and 31")
  Integer recurrenceDayOfMonth
) implements InstallmentPlan, RecurrencePlan {}
