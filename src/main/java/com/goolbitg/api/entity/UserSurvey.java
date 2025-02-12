package com.goolbitg.api.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.model.Day;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserSurvey
 */
@Entity
@Table(name = "user_surveys")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSurvey {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "check_1")
    private Boolean check1;

    @Column(name = "check_2")
    private Boolean check2;

    @Column(name = "check_3")
    private Boolean check3;

    @Column(name = "check_4")
    private Boolean check4;

    @Column(name = "check_5")
    private Boolean check5;

    @Column(name = "check_6")
    private Boolean check6;

    @Column(name = "avg_income_per_month")
    private Integer avgIncomePerMonth;

    @Column(name = "avg_saving_per_month")
    private Integer avgSavingPerMonth;

    @Column(name = "prime_use_day")
    @Enumerated(EnumType.STRING)
    private Day primeUseDay;

    @Column(name = "prime_use_time")
    private LocalTime primeUseTime;

    public Integer getChecklistScore() {
        if (check1 == null || check2 == null || check3 == null ||
            check4 == null || check5 == null)
            return null;
        int count = 0;
        if (check1) count++;
        if (check2) count++;
        if (check3) count++;
        if (check4) count++;
        if (check5) count++;
        return (int)((count + 1) / 2);
    }

    public Integer getSpendingHabitScore() {
        if (avgIncomePerMonth == null || avgSavingPerMonth == null)
            return null;
        return Math.round(100.0f - ((float)avgIncomePerMonth - avgSavingPerMonth) / (float)avgIncomePerMonth * 100.0f);
    }

    public void updateChecklist(
        boolean check1,
        boolean check2,
        boolean check3,
        boolean check4,
        boolean check5,
        boolean check6
    ) {
        this.check1 = check1;
        this.check2 = check2;
        this.check3 = check3;
        this.check4 = check4;
        this.check5 = check5;
        this.check6 = check6;
    }

    public void updateHabit(
        int avgIncomePerMonth,
        int avgSpendingPerMonth
    ) {
        this.avgIncomePerMonth = avgIncomePerMonth;
        this.avgSavingPerMonth = avgSpendingPerMonth;
    }

    public void updatePattern(Day primeUseDay, LocalTime primeUseTime) {
        this.primeUseDay = primeUseDay;
        this.primeUseTime = primeUseTime;
    }

    public static UserSurvey getDefault(String userId) {
        return UserSurvey.builder()
                .userId(userId)
                .build();
    }
}
