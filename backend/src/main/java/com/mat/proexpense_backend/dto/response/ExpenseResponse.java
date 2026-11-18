package com.mat.proexpense_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.mat.proexpense_backend.entitties.Expense;
import com.mat.proexpense_backend.enums.PaymentMethod;

public record ExpenseResponse(
  UUID id,
  UUID ownerId,
  UUID accountId,
  UUID categoryId,
  UUID cardId,
  String name,
  String description,
  BigDecimal amount,
  PaymentMethod paymentMethod,
  LocalDate expenseDate,
  boolean isInInstallments,
  int installmentsNumber,
  int currentInstallment
) {

  public static ExpenseResponse from(Expense expense) {
    return new ExpenseResponse(
        expense.getId(),
        expense.getOwner().getId(),
        expense.getAccount().getId(),
        expense.getCategory() != null ? expense.getCategory().getId() : null,
        expense.getCard().getId(),
        expense.getName(),
        expense.getDescription(),
        expense.getAmount(),
        expense.getPaymentMethod(),
        expense.getExpenseDate(),
        expense.isInInstallments(),
        expense.getInstallmentsNumber(),
        expense.getCurrentInstallment());
  }
}
