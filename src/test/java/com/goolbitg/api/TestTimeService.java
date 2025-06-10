package com.goolbitg.api;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.goolbitg.api.v1.service.TimeService;

/**
 * TestTimeService
 */
public class TestTimeService implements TimeService {

    LocalDateTime base = LocalDateTime.of(2025, 1, 23, 0, 0);
    int dayDelta = 0;

    private LocalDateTime now() {
        return base.plusDays(dayDelta);
    }

    public void increaseDay() {
        dayDelta += 1;
    }

    public void reset() {
        dayDelta = 0;
    }

    @Override
    public LocalDate getToday() {
        return now().toLocalDate();
    }

    @Override
    public LocalDateTime getNow() {
        return now();
    }

    @Override
    public Instant getNowInstant() {
        return now().toInstant(ZoneOffset.UTC);
    }

}
