package com.goolbitg.api.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.goolbitg.api.v1.entity.UserSurvey;
import org.junit.jupiter.api.Test;

/**
 * UserSurveyTest
 */
public class UserSurveyTest {

    @Test
    void get_spending_habit_score() {
        UserSurvey survey  = UserSurvey.builder()
                .avgIncomePerMonth(360000)
                .avgSavingPerMonth(200000)
                .build();

        assertEquals(56, survey.getSpendingHabitScore());
    }

    @Test
    void get_checklist_score_zero() {
        UserSurvey survey = UserSurvey.builder()
                .check1(false)
                .check2(false)
                .check3(false)
                .check4(false)
                .check5(false)
                .build();

        assertEquals(0, survey.getChecklistScore());
    }

    @Test
    void get_checklist_score_one() {
        UserSurvey survey1 = UserSurvey.builder()
                .check1(true)
                .check2(false)
                .check3(false)
                .check4(false)
                .check5(false)
                .build();
        UserSurvey survey2 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(false)
                .check4(false)
                .check5(false)
                .build();

        assertEquals(1, survey1.getChecklistScore());
        assertEquals(1, survey2.getChecklistScore());
    }

    @Test
    void get_checklist_score_two() {
        UserSurvey survey1 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(true)
                .check4(false)
                .check5(false)
                .build();
        UserSurvey survey2 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(false)
                .check4(true)
                .check5(true)
                .build();

        assertEquals(2, survey1.getChecklistScore());
        assertEquals(2, survey2.getChecklistScore());
    }

    @Test
    void get_checklist_score_three() {
        UserSurvey survey = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(true)
                .check4(true)
                .check5(true)
                .build();

        assertEquals(3, survey.getChecklistScore());
    }

}
