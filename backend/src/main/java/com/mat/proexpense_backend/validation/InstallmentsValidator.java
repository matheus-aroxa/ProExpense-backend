package com.mat.proexpense_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstallmentsValidator implements ConstraintValidator<ValidInstallments, InstallmentPlan> {

  private static final int MIN_INSTALLMENTS = 2;
  private static final int MAX_INSTALLMENTS = 120;

  @Override
  public boolean isValid(InstallmentPlan plan, ConstraintValidatorContext context) {
    if (plan == null) {
      return true;
    }

    int installments = plan.installmentsNumber();

    if (!plan.isInInstallments()) {
      return installments == 0 || reject(context, "Single-payment purchases must not specify installments");
    }

    boolean inRange = installments >= MIN_INSTALLMENTS && installments <= MAX_INSTALLMENTS;
    return inRange || reject(context, "Installment purchases must have between "
        + MIN_INSTALLMENTS + " and " + MAX_INSTALLMENTS + " installments");
  }

  private boolean reject(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode("installmentsNumber")
        .addConstraintViolation();
    return false;
  }
}
