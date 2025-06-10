package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.TestTimeService;
import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.v1.data.CronJobExecutor;
import com.goolbitg.api.v1.service.ChallengeGroupService;
import com.goolbitg.api.v1.service.TimeService;

/**
 * ChallengeGroupIntegrationTest
 */
@CustomIntegrationTest
public class ChallengeGroupIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TimeService timeService;
    @Autowired
    private ChallengeGroupService challengeGroupService;
    @Autowired
    private CronJobExecutor cronJobExecutor;
    @Autowired
    private ObjectMapper objectMapper;

    private final String ROOT_USER = "id0001";
    private final String NORMAL_USER = "id0003";
    private static ChallengeGroupDto group1;
    private static ChallengeGroupDto group2;

    @BeforeAll
    static void prepare() {
        group1 = new ChallengeGroupDto();
        group1.setTitle("title1");
        group1.setMaxSize(3);
        group1.setHashtags(List.of("tag1", "tag2"));
        group1.setReward(5000);
        group1.setIsHidden(false);

        group2 = new ChallengeGroupDto();
        group2.setTitle("title2");
        group2.setMaxSize(3);
        group2.setHashtags(List.of("tag2", "tag3"));
        group2.setReward(5000);
        group2.setIsHidden(true);
        group2.setPassword("1234");
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_groups() throws Exception {
        challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.createChallengeGroup(ROOT_USER, group2);

        mockMvc.perform(get("/api/v1/challengeGroups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(2));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_groups_by_keyword() throws Exception {
        challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.createChallengeGroup(ROOT_USER, group2);

        mockMvc.perform(get("/api/v1/challengeGroups")
            .param("search", "title1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(1));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_groups_i_created() throws Exception {
        challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.createChallengeGroup(NORMAL_USER, group2);

        mockMvc.perform(get("/api/v1/challengeGroups")
            .param("created", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(1));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_groups_i_participating() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.createChallengeGroup(ROOT_USER, group2);
        challengeGroupService.enrollChallengeGroup(ROOT_USER, create.getId());

        mockMvc.perform(get("/api/v1/challengeGroups")
            .param("participating", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(1))
                .andExpect(jsonPath("$.items[0].id").value(create.getId()));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void create_a_challenge_group() throws Exception {
        String content = objectMapper.writeValueAsString(group1);

        mockMvc.perform(post("/api/v1/challengeGroups")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(group1.getTitle()))
                .andExpect(jsonPath("$.reward").value(group1.getReward()))
                .andExpect(jsonPath("$.hashtags", Matchers.containsInAnyOrder(group1.getHashtags().toArray())))
                .andExpect(jsonPath("$.maxSize").value(group1.getMaxSize()))
                .andExpect(jsonPath("$.isHidden").value(group1.getIsHidden()))
                .andExpect(jsonPath("$.avgAchieveRatio").value(0))
                .andExpect(jsonPath("$.maxAchieveDays").value(0));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void create_a_challenge_group_with_password() throws Exception {
        String content = objectMapper.writeValueAsString(group2);

        mockMvc.perform(post("/api/v1/challengeGroups")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value(group2.getTitle()))
                .andExpect(jsonPath("$.reward").value(group2.getReward()))
                .andExpect(jsonPath("$.hashtags", Matchers.containsInAnyOrder(group2.getHashtags().toArray())))
                .andExpect(jsonPath("$.maxSize").value(group2.getMaxSize()))
                .andExpect(jsonPath("$.isHidden").value(group2.getIsHidden()))
                .andExpect(jsonPath("$.avgAchieveRatio").value(0))
                .andExpect(jsonPath("$.maxAchieveDays").value(0));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_a_challenge_group() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}", create.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.group.id").value(create.getId()))
                .andExpect(jsonPath("$.group.title").value(group1.getTitle()))
                .andExpect(jsonPath("$.group.reward").value(group1.getReward()))
                .andExpect(jsonPath("$.group.hashtags", Matchers.containsInAnyOrder(group1.getHashtags().toArray())))
                .andExpect(jsonPath("$.group.maxSize").value(group1.getMaxSize()))
                .andExpect(jsonPath("$.group.isHidden").value(group1.getIsHidden()))
                .andExpect(jsonPath("$.group.avgAchieveRatio").value(0))
                .andExpect(jsonPath("$.group.maxAchieveDays").value(0))
                .andExpect(jsonPath("$.rank").isArray());
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_group_rank() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.enrollChallengeGroup(ROOT_USER, create.getId());
        challengeGroupService.enrollChallengeGroup(NORMAL_USER, create.getId());
        challengeGroupService.checkChallengeGroup(ROOT_USER, create.getId());

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}", create.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rank[0].name").value("굴비왕"))
                .andExpect(jsonPath("$.rank[0].saving").value(5000))
                .andExpect(jsonPath("$.rank[1].name").value("굴비왕비"))
                .andExpect(jsonPath("$.rank[1].saving").value(0));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void update_a_challenge_group() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        create.setTitle("Updated Title");

        String content = objectMapper.writeValueAsString(create);

        mockMvc.perform(put("/api/v1/challengeGroups/{groupId}", create.getId())
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.reward").value(group1.getReward()))
                .andExpect(jsonPath("$.hashtags", Matchers.containsInAnyOrder(group1.getHashtags().toArray())))
                .andExpect(jsonPath("$.maxSize").value(group1.getMaxSize()))
                .andExpect(jsonPath("$.isHidden").value(group1.getIsHidden()))
                .andExpect(jsonPath("$.avgAchieveRatio").value(0))
                .andExpect(jsonPath("$.maxAchieveDays").value(0));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void delete_a_challenge_group() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);

        mockMvc.perform(delete("/api/v1/challengeGroups/{groupId}", create.getId()))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}", create.getId()))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(NORMAL_USER)
    void enroll_in_a_challenge_group() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);

        mockMvc.perform(post("/api/v1/challengeGroups/{groupId}/enroll", create.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(ROOT_USER)
    void check_challenge_group_record() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.enrollChallengeGroup(ROOT_USER, create.getId());

        mockMvc.perform(post("/api/v1/challengeGroups/{groupId}/check", create.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.challengeGroupId").value(create.getId()))
            .andExpect(jsonPath("$.userId").value(ROOT_USER))
            .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_group_tripple() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.enrollChallengeGroup(ROOT_USER, create.getId());
        challengeGroupService.checkChallengeGroup(ROOT_USER, create.getId());

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}/tripple", create.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.duration").value(1))
            .andExpect(jsonPath("$.check1").value("SUCCESS"))
            .andExpect(jsonPath("$.check2").value("WAIT"))
            .andExpect(jsonPath("$.check3").value("WAIT"))
            .andExpect(jsonPath("$.location").value(1));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_group_tripple_after_a_day_without_checking() throws Exception {
        TestTimeService testTimeService = (TestTimeService) timeService;

        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.enrollChallengeGroup(ROOT_USER, create.getId());
        challengeGroupService.checkChallengeGroup(ROOT_USER, create.getId());

        testTimeService.increaseDay();
        cronJobExecutor.finishTheDay();

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}/tripple", create.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.duration").value(2))
            .andExpect(jsonPath("$.check1").value("SUCCESS"))
            .andExpect(jsonPath("$.check2").value("WAIT"))
            .andExpect(jsonPath("$.check3").value("WAIT"))
            .andExpect(jsonPath("$.location").value(2));

        // NOTE: MUST RESET!!!
        testTimeService.reset();
    }
}

