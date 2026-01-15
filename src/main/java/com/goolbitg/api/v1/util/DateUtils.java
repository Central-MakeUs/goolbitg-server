package com.goolbitg.api.v1.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {

    public static DateRange getWeekRangeOfDate(LocalDate date) {
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek   = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return new DateRange(startOfWeek, endOfWeek);
    }

    public static record DateRange(LocalDate startDate, LocalDate endDate) {

    }
}
