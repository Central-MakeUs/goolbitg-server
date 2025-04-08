package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.ChallengeGroupDto;
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
    void create_a_challenge_group() throws Exception {
        String content = objectMapper.writeValueAsString(group1);

        mockMvc.perform(post("/api/v1/challengeGroups")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(ROOT_USER)
    void create_a_challenge_group_with_password() throws Exception {
        String content = objectMapper.writeValueAsString(group2);

        mockMvc.perform(post("/api/v1/challengeGroups")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_a_challenge_group() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}", create.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(create.getId()));
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
                .andExpect(jsonPath("$.title").value("Updated Title"));
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
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(ROOT_USER)
    void check_challenge_group_record() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);

        mockMvc.perform(get("/api/v1/challengeGroups/{groupId}/records", create.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.challengeGroupId").value(create.getId()));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void enroll_and_check_challenge_group_record() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        Long groupId = create.getId();
        challengeGroupService.enrollChallengeGroup(ROOT_USER, groupId);

        mockMvc.perform(get("/api/v1/challengeGroupRecords/{groupId}", create.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.challengeGroupId").value(create.getId()))
            .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_group_records_for_today() throws Exception {
        challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        challengeGroupService.createChallengeGroup(ROOT_USER, group2);

        mockMvc.perform(get("/api/v1/challengeGroupRecords"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalSize").value(2));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_challenge_group_stats() throws Exception {
        ChallengeGroupDto create = challengeGroupService.createChallengeGroup(ROOT_USER, group1);
        Long groupId = create.getId();

        mockMvc.perform(get("/api/v1/challengeGroupStats/{groupId}", groupId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.groupId").value(groupId))
            .andExpect(jsonPath("$.userId").value(ROOT_USER))
            .andExpect(jsonPath("$.continueCount").value(0))
            .andExpect(jsonPath("$.totalCount").value(0))
            .andExpect(jsonPath("$.enrollCount").value(0));

        challengeGroupService.enrollChallengeGroup(ROOT_USER, groupId);

        mockMvc.perform(get("/api/v1/challengeGroupStats/{groupId}", groupId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.enrollCount").value(1));

        challengeGroupService.checkChallengeGroup(ROOT_USER, groupId);

        mockMvc.perform(get("/api/v1/challengeGroupStats/{groupId}", groupId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.continueCount").value(1))
            .andExpect(jsonPath("$.totalCount").value(1));
    }
}
