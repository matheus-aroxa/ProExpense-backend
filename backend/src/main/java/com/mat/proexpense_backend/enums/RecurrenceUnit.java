package com.mat.proexpense_backend.enums;

import java.time.LocalDate;

public enum RecurrenceUnit {
  DAYS,
  MONTHS,
  YEARS;

  public LocalDate addTo(LocalDate date, long amount) {
    return switch (this) {
      case DAYS -> date.plusDays(amount);
      case MONTHS -> date.plusMonths(amount);
      case YEARS -> date.plusYears(amount);
    };
  }
}
