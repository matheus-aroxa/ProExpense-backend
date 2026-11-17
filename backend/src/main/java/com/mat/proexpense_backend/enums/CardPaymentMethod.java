package com.mat.proexpense_backend.enums;

public enum CardPaymentMethod {
  CREDIT,
  DEBIT,
  BOTH;

  public boolean supports(PaymentMethod paymentMethod) {
    return switch (this) {
      case CREDIT -> paymentMethod == PaymentMethod.CREDIT;
      case DEBIT -> paymentMethod == PaymentMethod.DEBIT;
      case BOTH -> paymentMethod == PaymentMethod.CREDIT || paymentMethod == PaymentMethod.DEBIT;
    };
  }
}
