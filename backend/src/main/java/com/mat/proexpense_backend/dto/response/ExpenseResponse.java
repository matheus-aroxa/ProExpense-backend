package com.mat.proexpense_backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.enums.PaymentMethod;

public record ExpenseResponse(
  UUID id,
  UUID ownerId,
  String name,
  String description,
  BigDecimal amount,
  PaymentMethod paymentMethod,
  boolean isInInstallments,
  int installmentsNumber
) {

  public static ExpenseResponse from(Expense expense) {
    return new ExpenseResponse(
        expense.getId(),
        expense.getOwnerId(),
        expense.getName(),
        expense.getDescription(),
        expense.getAmount(),
        expense.getPaymentMethod(),
        expense.isInInstallments(),
        expense.getInstallmentsNumber());
  }
}
