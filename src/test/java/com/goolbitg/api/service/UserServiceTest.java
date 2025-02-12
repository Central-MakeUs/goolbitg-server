package com.goolbitg.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.goolbitg.api.entity.UserSurvey;

/**
 * UserServiceTest
 */
public class UserServiceTest {

    UserService userService = new UserServiceImpl();

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
        
        assertEquals(1L, userService.determineSpendingType(survey1));
        assertEquals(2L, userService.determineSpendingType(survey2));
        assertEquals(3L, userService.determineSpendingType(survey3));
        assertEquals(4L, userService.determineSpendingType(survey4));
        assertEquals(5L, userService.determineSpendingType(survey5));
    }
}
