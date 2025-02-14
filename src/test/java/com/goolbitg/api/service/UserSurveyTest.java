package com.goolbitg.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.goolbitg.api.entity.UserSurvey;

/**
 * UserServiceTest
 */
public class UserSurveyTest {

    @Test
    void determine_spending_type() {
        UserSurvey survey1 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(true)
                .check4(true)
                .check5(true)
                .avgIncomePerMonth(1_000_000)
                .avgSavingPerMonth(400_000)
                .build();
        UserSurvey survey2 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(true)
                .check4(true)
                .check5(true)
                .avgIncomePerMonth(1_000_000)
                .avgSavingPerMonth(600_000)
                .build();
        UserSurvey survey3 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(false)
                .check4(true)
                .check5(true)
                .avgIncomePerMonth(1_000_000)
                .avgSavingPerMonth(750_000)
                .build();
        UserSurvey survey4 = UserSurvey.builder()
                .check1(true)
                .check2(true)
                .check3(false)
                .check4(true)
                .check5(false)
                .avgIncomePerMonth(1_000_000)
                .avgSavingPerMonth(850_000)
                .build();
        UserSurvey survey5 = UserSurvey.builder()
                .check1(false)
                .check2(false)
                .check3(false)
                .check4(false)
                .check5(false)
                .avgIncomePerMonth(1_000_000)
                .avgSavingPerMonth(900_000)
                .build();
        
        assertEquals(1L, survey1.getSpendingTypeId());
        assertEquals(2L, survey2.getSpendingTypeId());
        assertEquals(3L, survey3.getSpendingTypeId());
        assertEquals(4L, survey4.getSpendingTypeId());
        assertEquals(5L, survey5.getSpendingTypeId());
    }
}
