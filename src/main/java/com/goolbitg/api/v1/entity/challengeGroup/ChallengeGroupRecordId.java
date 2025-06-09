package com.goolbitg.api.v1.entity.challengeGroup;

import java.time.LocalDate;

/**
 * ChallengeGroupRecordId
 */
public record ChallengeGroupRecordId(Long groupId, String userId, LocalDate date) {
}
