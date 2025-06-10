package com.goolbitg.api.v1.entity.challengeGroup;

import java.time.LocalDate;

/**
 * ChallengeGroupRecordId
 */
public record ChallengeGroupRecordId(Long groupId, String userId, LocalDate date) {

    public ChallengeGroupRecordId next() {
        return new ChallengeGroupRecordId(groupId, userId, date.plusDays(1));
    }

    public ChallengeGroupRecordId prev() {
        return new ChallengeGroupRecordId(groupId, userId, date.minusDays(1));
    }

}
