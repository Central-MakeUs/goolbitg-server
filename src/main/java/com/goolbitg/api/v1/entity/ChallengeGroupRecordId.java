package com.goolbitg.api.v1.entity;

import java.io.Serializable;
import java.time.LocalDate;

public record ChallengeGroupRecordId(Long groupId, String userId, LocalDate date) implements Serializable {

}

