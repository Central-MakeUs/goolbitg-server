package com.goolbitg.api.v1.entity;

import java.time.LocalDate;

/**
 * ChallengeGroupRecordId
 */
public record ChallengeGroupRecordId(Long groupId, String userId, LocalDate date) {
}
