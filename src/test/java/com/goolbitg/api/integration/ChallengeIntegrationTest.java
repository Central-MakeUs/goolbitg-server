package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChallengeIntegrationTest
 */
@CustomIntegrationTest
public class ChallengeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String ROOT_USER = "id0001";
    private final String NORMAL_USER = "id0003";

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void get_a_challenge() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(get("/challenges/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantCount").value(1))
                .andExpect(jsonPath("$.avgAchieveRatio").value(100.0))
                .andExpect(jsonPath("$.maxAchieveDays").value(3));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void get_challenges_for_type() throws Exception {
        Integer page = 0;
        Integer size = 10;

        mockMvc.perform(get("/challenges")
            .param("page", page.toString())
            .param("size", size.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.items.length()").value(10))
                .andExpect(jsonPath("$.items[0].id").value(5));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void get_a_challenge_record_today() throws Exception {
        Long challengeId = 2L;
        mockMvc.perform(get("/challengeRecords/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("WAIT"))
                .andExpect(jsonPath("$.duration").value(2));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void get_challenge_records() throws Exception {
        mockMvc.perform(get("/challengeRecords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReward").value(7000))
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void get_challenge_records_of_specific_status() throws Exception {
        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-17")
            .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void enroll_challenge() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-23"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(2));
        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void enroll_new_challenge() throws Exception {
        Long challengeId = 3L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challengeCount").value(3));
        mockMvc.perform(get("/challengeStat/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollCount").value(1));
        mockMvc.perform(get("/users/me/weeklyStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyStatus.length()").value(7))
                .andExpect(jsonPath("$.weeklyStatus[3].totalChallenges").value(2));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void enroll_experienced_challenge() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challengeCount").value(2));
    }

    @Test
    @Transactional
    @WithMockUser(NORMAL_USER)
    void reject_enroll_when_already_enrolled() throws Exception {
        Long challengeId = 2L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value("4001"));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void stat_update_when_check() throws Exception {
        Long challengeId = 2L;
        mockMvc.perform(post("/challengeRecords/{challengeId}/check", challengeId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/challengeStat/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(5))
                .andExpect(jsonPath("$.continueCount").value(3));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void check_challenge() throws Exception {
        Long challengeId = 2L;
        mockMvc.perform(post("/challengeRecords/{challengeId}/check", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.achievementGuage").value(9000));
        mockMvc.perform(get("/challengeStat/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.continueCount").value(3))
                .andExpect(jsonPath("$.totalCount").value(5));
        mockMvc.perform(get("/users/me/weeklyStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyStatus[3].achievedChallenges").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void check_and_enroll_challenge() throws Exception {
        Long challengeId = 2L;
        mockMvc.perform(post("/challengeRecords/{challengeId}/check", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"));
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/users/me/weeklyStatus"))
                .andExpect(jsonPath("$.weeklyStatus[3].totalChallenges").value(1));
        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
        mockMvc.perform(get("/challengeRecords")
            .param("date", "2025-01-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    @Disabled
    void cancel_challenge() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());
        mockMvc.perform(delete("/challengeRecords/{challengeId}", challengeId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/challengeRecords/{challengeId}", challengeId))
                .andExpect(status().isUnprocessableEntity());
        mockMvc.perform(get("/challengeStat/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollCount").value(1));
        mockMvc.perform(get("/users/me/weeklyStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weeklyStatus[3].totalChallenges").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER)
    void get_challenge_stat() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(get("/challengeStat/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challengeId").value(challengeId));
    }

    @Test
    @Transactional
    @WithMockUser(NORMAL_USER)
    void get_challenge_tripple() throws Exception {
        Long challengeId = 2L;
        mockMvc.perform(get("/challengeTripple/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.challenge.id").value(challengeId))
                .andExpect(jsonPath("$.check1").value("SUCCESS"))
                .andExpect(jsonPath("$.check2").value("WAIT"))
                .andExpect(jsonPath("$.check3").value("WAIT"))
                .andExpect(jsonPath("$.location").value(2))
                .andExpect(jsonPath("$.canceled").value(false));
    }

    @Test
    @Transactional
    @WithMockUser(NORMAL_USER)
    @Disabled
    void enroll_check_cancel_get_tripple() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/challengeRecords/{challengeId}/check", challengeId))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/challengeRecords/{challengeId}", challengeId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/challengeTripple/{challengeId}", challengeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.check1").value("SUCCESS"))
                .andExpect(jsonPath("$.check2").value("FAIL"))
                .andExpect(jsonPath("$.check3").value("FAIL"))
                .andExpect(jsonPath("$.canceled").value(true));
    }

    @Test
    @Transactional
    @WithMockUser(NORMAL_USER)
    void enroll_check_enroll_again_fail() throws Exception {
        Long challengeId = 1L;
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/challengeRecords/{challengeId}/check", challengeId))
                .andExpect(status().isOk());
        mockMvc.perform(post("/challenges/{challengeId}/enroll", challengeId))
                .andExpect(status().isUnprocessableEntity());
    }
}
