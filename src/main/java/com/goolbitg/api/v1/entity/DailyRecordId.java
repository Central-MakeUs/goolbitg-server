package com.goolbitg.api.v1.entity;

import java.time.LocalDate;

/**
 * DailyRecordId
 */
public record DailyRecordId(String userId, LocalDate date) {
}
