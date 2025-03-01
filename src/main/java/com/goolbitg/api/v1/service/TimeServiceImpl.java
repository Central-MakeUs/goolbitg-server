package com.goolbitg.api.v1.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * TimeServiceImpl
 */
@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService {

    @Autowired
    private final Clock clock;

    @Override
    public LocalDate getToday() {
        return LocalDate.now(clock);
    }

    @Override
    public LocalDateTime getNow() {
        return LocalDateTime.now(clock);
    }

    @Override
    public Instant getNowInstant() {
        LocalDateTime now = getNow();
        return now.toInstant(ZoneOffset.UTC);
    }

}
