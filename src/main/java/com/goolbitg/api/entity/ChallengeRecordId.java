package com.goolbitg.api.entity;

import java.time.LocalDate;

/**
 * ChallengeRecordId
 */
public record ChallengeRecordId(Long challengeId, String userId, LocalDate date) {
}
