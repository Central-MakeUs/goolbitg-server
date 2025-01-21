package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * ChallengeStat
 */
@Entity
@Table(name = "challenge_stats")
@Getter
@Setter
public class ChallengeStat {

    @Id
    @Column(name = "challenge_id")
    private Long challengeId;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "avg_achive_ratio")
    private Float avgAchiveRatio;

    @Column(name = "max_achive_days")
    private Integer maxAchiveDays;

}
