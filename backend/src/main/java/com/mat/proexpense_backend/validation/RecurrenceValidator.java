package com.mat.proexpense_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RecurrenceValidator implements ConstraintValidator<ValidRecurrence, RecurrencePlan> {

  @Override
  public boolean isValid(RecurrencePlan plan, ConstraintValidatorContext context) {
    if (plan == null) {
      return true;
    }

    boolean hasInterval = plan.recurrenceUnit() != null || plan.recurrenceInterval() != null;
    boolean hasDayOfMonth = plan.recurrenceDayOfMonth() != null;

    if (!plan.isInInstallments()) {
      if (hasDayOfMonth) {
        return reject(context, "Single-payment purchases must not specify recurrence", "recurrenceDayOfMonth");
      }
      if (plan.recurrenceUnit() != null) {
        return reject(context, "Single-payment purchases must not specify recurrence", "recurrenceUnit");
      }
      if (plan.recurrenceInterval() != null) {
        return reject(context, "Single-payment purchases must not specify recurrence", "recurrenceInterval");
      }
      return true;
    }

    return !(hasInterval && hasDayOfMonth)
        || reject(context, "Specify recurrence by interval or by fixed day, not both", "recurrenceDayOfMonth");
  }

  private boolean reject(ConstraintValidatorContext context, String message, String field) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message)
        .addPropertyNode(field)
        .addConstraintViolation();
    return false;
  }
}
