package com.goolbitg.api.v1.entity;

import java.io.Serializable;

public record ChallengeGroupStatsId(Long groupId, String userId) implements Serializable {
}

