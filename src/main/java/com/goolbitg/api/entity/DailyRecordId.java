package com.goolbitg.api.entity;

import java.time.LocalDate;

/**
 * DailyRecordId
 */
public record DailyRecordId(String userId, LocalDate date) {
}
