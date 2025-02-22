package com.goolbitg.api.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TimeService
 */
public interface TimeService {

    LocalDate getToday();
    LocalDateTime getNow();
    Instant getNowInstant();

}
