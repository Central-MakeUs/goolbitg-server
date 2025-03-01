package com.goolbitg.api.v1.service;

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
