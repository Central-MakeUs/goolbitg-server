package com.goolbitg.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private boolean check1;

    @Column(name = "check_2")
    private boolean check2;

    @Column(name = "check_3")
    private boolean check3;

    @Column(name = "check_4")
    private boolean check4;

    @Column(name = "check_5")
    private boolean check5;

    @Column(name = "check_6")
    private boolean check6;

    @Column(name = "avg_income_per_month")
    private int avgIncomePerMonth;

    @Column(name = "avg_spending_per_month")
    private int avgSpendingPerMonth;

    @Column(name = "spending_habit_score")
    private int spendingHabitScore;

    @Column(name = "prime_use_day")
    private Day primUseDay;

    @Column(name = "prime_use_time")
    private LocalDateTime primeUseTime;

}
