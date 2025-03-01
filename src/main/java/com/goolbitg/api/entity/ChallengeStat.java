package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ChallengeUserStat
 */
@Entity
@Table(name = "challenge_stats")
@IdClass(ChallengeStatId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeStat {

    @Id
    @Column(name = "challenge_id")
    private Long challengeId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "continue_count")
    private Integer continueCount;

    @Column(name = "current_continue_count")
    private Integer currentContinueCount;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "enroll_count")
    private Integer enrollCount;

    public void increaseCount() {
        totalCount += 1;
        currentContinueCount += 1;
        continueCount = Math.max(currentContinueCount, continueCount);
    }

    public void enroll() {
        enrollCount += 1;
    }

    public void cancel() {
        enrollCount -= 1;
        continueCount = 0;
    }

    public void fail() {
        continueCount = 0;
    }

    public static ChallengeStat getDefault(Long challengeId, String userId) {
        return ChallengeStat.builder()
                .challengeId(challengeId)
                .userId(userId)
                .continueCount(0)
                .currentContinueCount(0)
                .totalCount(0)
                .enrollCount(0)
                .build();
    }
}
