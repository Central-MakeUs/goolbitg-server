package com.goolbitg.api.entity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.model.Day;

import lombok.Getter;
import lombok.Setter;

/**
 * UserSurvey
 */
@Entity
@Getter
@Setter
@Table(name = "user_surveys")
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

    @Column(name = "avg_spending_per_month")
    private Integer avgSpendingPerMonth;

    @Column(name = "spending_habit_score")
    private Integer spendingHabitScore;

    @Column(name = "prime_use_day")
    @Enumerated(EnumType.STRING)
    private Day primUseDay;

    @Column(name = "prime_use_time")
    private LocalTime primeUseTime;

}
