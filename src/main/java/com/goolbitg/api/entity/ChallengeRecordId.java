package com.goolbitg.api.entity;

import java.time.LocalDate;

/**
 * ChallengeRecordId
 */
public record ChallengeRecordId(Long challengeId, String userId, LocalDate date) {
    
    public ChallengeRecordId next() {
        return new ChallengeRecordId(challengeId, userId, date.plusDays(1));
    }

    public ChallengeRecordId prev() {
        return new ChallengeRecordId(challengeId, userId, date.minusDays(1));
    }
}
