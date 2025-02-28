package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.goolbitg.api.data.CronJobExecutor;
import com.goolbitg.api.service.ChallengeService;
import com.goolbitg.api.service.TimeService;

/**
 * CronJobExecutorTest
 */
@CustomIntegrationTest
public class CronJobExecutorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private CronJobExecutor cronJobExecutor;
    @Autowired
    private TimeService timeService;

    private final static String ROOT_USER = "id0001";

    @Test
    @WithMockUser(ROOT_USER)
    void cancel_unchecked_challenges() throws Exception {
        Long challengeId = 1L;
        LocalDate today = timeService.getToday();

        challengeService.enrollChallenge(ROOT_USER, challengeId, today.minusDays(1));
        cronJobExecutor.finishTheDay();

        mockMvc.perform(get("/challengeRecords/{challengeId}", challengeId)
            .param("date", today.minusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAIL"));
        mockMvc.perform(get("/challengeRecords/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAIL"));
        mockMvc.perform(get("/challengeRecords/{challengeId}", challengeId)
            .param("date", today.plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAIL"));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void create_daily_records() throws Exception {
        Long challengeId = 1L;
        LocalDate today = timeService.getToday();

        challengeService.enrollChallenge(ROOT_USER, challengeId, today.minusDays(1));
        challengeService.checkChallenge(ROOT_USER, challengeId, today.minusDays(1));
        cronJobExecutor.finishTheDay();

        mockMvc.perform(get("/users/me/weeklyStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyStatus[2].totalChallenges").value(1))
                .andExpect(jsonPath("$.weeklyStatus[2].achievedChallenges").value(1))
                .andExpect(jsonPath("$.weeklyStatus[3].totalChallenges").value(1))
                .andExpect(jsonPath("$.weeklyStatus[3].achievedChallenges").value(0));
    }
}
