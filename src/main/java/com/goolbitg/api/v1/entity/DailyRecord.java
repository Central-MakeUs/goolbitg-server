package com.goolbitg.api.v1.entity;

import java.time.LocalDate;

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
 * DailyRecord
 */
@Entity
@Table(name = "daily_records")
@IdClass(DailyRecordId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void enroll() {
        totalChallenges += 1;
    }

    public void achieve(int reward) {
        saving += reward;
        achievedChallenges += 1;
    }

    public void cancel() {
        totalChallenges -= 1;
    }

    public boolean isCompleted() {
        return totalChallenges > 0 && achievedChallenges == totalChallenges;
    }

    public static DailyRecord getDefault(String userId, LocalDate date) {
        return DailyRecord.builder()
            .userId(userId)
            .date(date)
            .saving(0)
            .totalChallenges(0)
            .achievedChallenges(0)
            .build();
    }

}
