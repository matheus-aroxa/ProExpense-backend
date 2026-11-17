package com.mat.proexpense_backend.validation;

import com.mat.proexpense_backend.enums.RecurrenceUnit;

public interface RecurrencePlan {

  boolean isInInstallments();

  RecurrenceUnit recurrenceUnit();

  Integer recurrenceInterval();

  Integer recurrenceDayOfMonth();
}
