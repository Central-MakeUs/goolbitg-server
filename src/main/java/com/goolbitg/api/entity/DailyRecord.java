package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * DailyRecord
 */
@Entity
@Table(name = "daily_records")
@IdClass(DailyRecordId.class)
@Getter
@Setter
public class DailyRecord {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "saving")
    private Integer saving;

    @Column(name = "total_challenges")
    private Integer totalChallenges;

    @Column(name = "achieved_challenges")
    private Integer achievedChallenges;

    public void achieve(int reward) {
        saving += reward;
        achievedChallenges += 1;
    }

    public static DailyRecord getDefault(String userId, LocalDate date) {
        DailyRecord entity = new DailyRecord();
        entity.setUserId(userId);
        entity.setDate(date);
        entity.setSaving(0);
        entity.setTotalChallenges(0);
        entity.setAchievedChallenges(0);
        return entity;
    }

}
