package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * ChallengeUserStat
 */
@Entity
@Table(name = "challenge_stats")
@IdClass(ChallengeStatId.class)
@Getter
@Setter
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

}
