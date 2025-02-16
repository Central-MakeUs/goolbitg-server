package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotReportRequest;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.BuyOrNotVoteType;

/**
 * BuyOrNotIntegrationTest
 */
@CustomIntegrationTest
public class BuyOrNotIntegrationTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private  ObjectMapper mapper;

    private final String ROOT_USER_ID = "id0001";
    private final String NORMAL_USER_ID = "id0003";

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void get_a_buy_or_not() throws Exception {
        Long postId = 1L;
        mockMvc.perform(get("/buyOrNots/{postId}", postId))
                    .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void get_buy_or_not_list() throws Exception {
        mockMvc.perform(get("/buyOrNots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void create_new_post() throws Exception {
        BuyOrNotDto request = new BuyOrNotDto();
        request.setProductName("에어팟");
        request.setProductPrice(129000);
        request.setProductImageUrl(URI.create("test_url"));
        request.setGoodReason("노래들을때 좋음");
        request.setBadReason("비쌈");
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/buyOrNots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void update_post() throws Exception {
        Long postId = 1L;
        BuyOrNotDto request = new BuyOrNotDto();
        request.setProductName("에어팟");
        request.setGoodReason("노래들을때 좋음");
        request.setBadReason("비쌈");
        request.setProductImageUrl(URI.create("example_url"));
        request.setProductPrice(100000);
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(put("/buyOrNots/{postId}", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("에어팟"));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void delete_post() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete("/buyOrNots/{postId}", postId))
                .andExpect(status().isOk());
        mockMvc.perform(get("/buyOrNots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(0));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void vote_buy_or_not() throws Exception {
        Long postId = 1L;
        BuyOrNotVoteDto request = new BuyOrNotVoteDto();
        request.setVote(BuyOrNotVoteType.BAD);
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/buyOrNots/{postId}/vote", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodVoteCount").value(0))
                .andExpect(jsonPath("$.badVoteCount").value(1));
        mockMvc.perform(get("/buyOrNots/{podtId}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodVoteCount").value(0))
                .andExpect(jsonPath("$.badVoteCount").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(NORMAL_USER_ID)
    void report_post() throws Exception {
        Long postId = 1L;
        BuyOrNotReportRequest request = new BuyOrNotReportRequest();
        request.setReason("잘못된 정보");
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/buyOrNots/{postId}/report", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(NORMAL_USER_ID)
    void get_filtered_result() throws Exception {
        Long postId = 1L;
        BuyOrNotReportRequest request = new BuyOrNotReportRequest();
        request.setReason("잘못된 정보");
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/buyOrNots/{postId}/report", postId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
        mockMvc.perform(get("/buyOrNots"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalSize").value(0));
    }

}
