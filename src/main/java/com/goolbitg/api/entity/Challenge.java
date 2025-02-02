package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Challenge
 */
@Entity
@Table(name = "challenges")
@Getter
@Setter
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url_small")
    private String imageUrlSmall;

    @Column(name = "image_url_large")
    private String imageUrlLarge;

    @Column(name = "reward")
    private Integer reward;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "max_achieve_days")
    private Integer maxAchieveDays;

    @Column(name = "total_records")
    private Integer totalRecords;

    @Column(name = "achieved_records")
    private Integer achievedRecords;

    public Float getAvgAchieveRatio() {
        if (totalRecords == null || achievedRecords == null) return null;
        if (achievedRecords == 0) return 0f;
        return (float)totalRecords / achievedRecords * 100;
    }

}
