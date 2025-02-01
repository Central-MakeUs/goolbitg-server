package com.goolbitg.api.service;

import java.time.Clock;
import java.time.LocalDate;

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

}
