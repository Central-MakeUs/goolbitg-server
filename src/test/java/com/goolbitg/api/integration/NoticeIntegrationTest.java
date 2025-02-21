package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * NoticeIntegrationTest
 */
@CustomIntegrationTest
public class NoticeIntegrationTest {

    private final String ROOT_USER_ID = "id0001";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void get_notices() throws Exception {
        mockMvc.perform(get("/notices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size").value(2));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void get_notices_of_type() throws Exception {
        mockMvc.perform(get("/notices")
            .param("type", "CHALLENGE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void read_notice() throws Exception {
        Long noticeId = 1L;
        mockMvc.perform(post("/notices/{noticeId}", noticeId))
            .andExpect(status().isOk());

        mockMvc.perform(get("/notices")
            .param("type", "CHALLENGE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].read").value(true));
    }

}
