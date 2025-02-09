package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Challenge
 */
@Entity
@Table(name = "challenges")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        if (totalRecords == 0) return 0f;
        return (float)(achievedRecords / totalRecords) * 100;
    }

    public void enroll() {
        totalRecords += 3;
    }

    public void achieve() {
        achievedRecords += 1;
    }

    public void updateStats(int participantCount, int maxAchieveDays) {
        this.participantCount = participantCount;
        this.maxAchieveDays = maxAchieveDays;
    }

}
