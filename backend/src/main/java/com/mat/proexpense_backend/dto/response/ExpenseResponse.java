package com.mat.proexpense_backend.dto.response;

import java.math.BigDecimal;
import com.mat.proexpense_backend.enums.PaymentMethod;

public record ExpenseResponse(
  String name,
  String description,
  BigDecimal amount,
  PaymentMethod paymentMethod,
  boolean isInInstallments,
  int installmentsNumber
) {}
