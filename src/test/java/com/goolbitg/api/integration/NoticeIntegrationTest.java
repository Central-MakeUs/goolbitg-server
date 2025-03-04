package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.goolbitg.api.model.NoticeDto;
import com.goolbitg.api.model.NoticeType;
import com.goolbitg.api.v1.service.NoticeService;

/**
 * NoticeIntegrationTest
 */
@CustomIntegrationTest
public class NoticeIntegrationTest {

    private final String ROOT_USER = "id0001";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NoticeService noticeService;

    @Test
    @WithMockUser(ROOT_USER)
    void get_notices() throws Exception {
        String message1 = "message1";
        String message2 = "message2";
        NoticeType type1 = NoticeType.VOTE;
        NoticeType type2 = NoticeType.CHALLENGE;

        noticeService.sendMessage(ROOT_USER, message1, type1);
        noticeService.sendMessage(ROOT_USER, message2, type2);

        mockMvc.perform(get("/api/v1/notices"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size").value(2))
            .andExpect(jsonPath("$.items[0].message").value(message2))
            .andExpect(jsonPath("$.items[1].message").value(message1));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_notices_of_type() throws Exception {
        String message1 = "message1";
        String message2 = "message2";
        NoticeType type1 = NoticeType.VOTE;
        NoticeType type2 = NoticeType.CHALLENGE;

        noticeService.sendMessage(ROOT_USER, message1, type1);
        noticeService.sendMessage(ROOT_USER, message2, type2);

        mockMvc.perform(get("/api/v1/notices")
            .param("type", "CHALLENGE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void read_notice() throws Exception {
        String message1 = "message1";
        NoticeType type1 = NoticeType.CHALLENGE;

        NoticeDto saved = noticeService.sendMessage(ROOT_USER, message1, type1);

        mockMvc.perform(post("/api/v1/notices/{noticeId}", saved.getId()))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/notices")
            .param("type", "CHALLENGE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].read").value(true));
    }

}
