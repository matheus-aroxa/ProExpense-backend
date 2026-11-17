package com.mat.proexpense_backend.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.mat.proexpense_backend.enums.RecurrenceUnit;

public final class InstallmentScheduler {

  private InstallmentScheduler() {
  }

  public static List<LocalDate> byInterval(LocalDate start, int installments, RecurrenceUnit unit, int interval) {
    return IntStream.range(0, installments)
        .mapToObj(index -> unit.addTo(start, (long) interval * index))
        .toList();
  }

  public static List<LocalDate> byDayOfMonth(LocalDate start, int installments, int dayOfMonth) {
    List<LocalDate> dates = new ArrayList<>();
    dates.add(start);

    YearMonth month = YearMonth.from(start);
    if (!atDay(month, dayOfMonth).isAfter(start)) {
      month = month.plusMonths(1);
    }

    for (int index = 1; index < installments; index++) {
      dates.add(atDay(month, dayOfMonth));
      month = month.plusMonths(1);
    }

    return dates;
  }

  private static LocalDate atDay(YearMonth month, int dayOfMonth) {
    return month.atDay(Math.min(dayOfMonth, month.lengthOfMonth()));
  }
}
