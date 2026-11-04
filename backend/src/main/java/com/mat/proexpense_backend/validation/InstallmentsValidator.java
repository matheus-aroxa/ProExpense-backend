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
      return installments == 0 || reject(context, "Compras à vista não devem informar parcelas");
    }

    boolean inRange = installments >= MIN_INSTALLMENTS && installments <= MAX_INSTALLMENTS;
    return inRange || reject(context, "Compras parceladas devem ter entre "
        + MIN_INSTALLMENTS + " e " + MAX_INSTALLMENTS + " parcelas");
  }

  private boolean reject(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode("installmentsNumber")
        .addConstraintViolation();
    return false;
  }
}
