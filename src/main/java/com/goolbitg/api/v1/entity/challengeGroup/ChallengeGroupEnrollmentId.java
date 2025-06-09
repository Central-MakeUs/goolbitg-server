package com.goolbitg.api.v1.entity.challengeGroup;

import java.io.Serializable;

public record ChallengeGroupEnrollmentId(Long groupId, String userId) implements Serializable {
}

