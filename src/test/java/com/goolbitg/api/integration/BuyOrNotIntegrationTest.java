package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotReportRequest;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.BuyOrNotVoteType;
import com.goolbitg.api.v1.service.BuyOrNotService;

/**
 * BuyOrNotIntegrationTest
 */
@CustomIntegrationTest
public class BuyOrNotIntegrationTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private BuyOrNotService buyOrNotService;

    private final String ROOT_USER = "id0001";
    private final String NORMAL_USER = "id0003";

    private BuyOrNotDto post1;
    private BuyOrNotDto post2;
    private BuyOrNotDto post3;

    @BeforeEach
    void preparePost() {
        post1 = new BuyOrNotDto();
        post1.setProductName("Product 1");
        post1.setProductImageUrl(URI.create("http://localhost:8080/image/1"));
        post1.setProductPrice(50000);
        post1.setGoodReason("Good Reason");
        post1.setBadReason("Bad Reason");

        post2 = new BuyOrNotDto();
        post2.setProductName("Product 2");
        post2.setProductImageUrl(URI.create("http://localhost:8080/image/2"));
        post2.setProductPrice(50000);
        post2.setGoodReason("Good Reason");
        post2.setBadReason("Bad Reason");

        post3 = new BuyOrNotDto();
        post3.setProductName("Product 3");
        post3.setProductImageUrl(URI.create("http://localhost:8080/image/3"));
        post3.setProductPrice(50000);
        post3.setGoodReason("Good Reason");
        post3.setBadReason("Bad Reason");
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_a_buy_or_not() throws Exception {
        BuyOrNotDto created = buyOrNotService.createBuyOrNot(ROOT_USER, post1);

        mockMvc.perform(get("/api/v1/buyOrNots/{postId}", created.getId()))
                    .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(ROOT_USER)
    void get_buy_or_not_list() throws Exception {
        BuyOrNotDto created1 = buyOrNotService.createBuyOrNot(ROOT_USER, post1);
        BuyOrNotDto created2 = buyOrNotService.createBuyOrNot(ROOT_USER, post2);
        BuyOrNotDto created3 = buyOrNotService.createBuyOrNot(ROOT_USER, post3);

        mockMvc.perform(get("/api/v1/buyOrNots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(3))
                .andExpect(jsonPath("$.items[0].id").value(created3.getId()))
                .andExpect(jsonPath("$.items[1].id").value(created2.getId()))
                .andExpect(jsonPath("$.items[2].id").value(created1.getId()));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void create_new_post() throws Exception {
        String jsonBody = mapper.writeValueAsString(post1);

        mockMvc.perform(post("/api/v1/buyOrNots")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.writerId").value(ROOT_USER))
                .andExpect(jsonPath("$.productName").value(post1.getProductName()))
                .andExpect(jsonPath("$.productPrice").value(post1.getProductPrice()))
                .andExpect(jsonPath("$.productImageUrl").value(post1.getProductImageUrl().toString()))
                .andExpect(jsonPath("$.goodReason").value(post1.getGoodReason()))
                .andExpect(jsonPath("$.badReason").value(post1.getBadReason()));
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postCount").value(1));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void update_post() throws Exception {
        BuyOrNotDto previous = buyOrNotService.createBuyOrNot(ROOT_USER, post1);
        String jsonBody = mapper.writeValueAsString(post2);

        mockMvc.perform(put("/api/v1/buyOrNots/{postId}", previous.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.writerId").value(ROOT_USER))
                .andExpect(jsonPath("$.productName").value(post2.getProductName()))
                .andExpect(jsonPath("$.productPrice").value(post2.getProductPrice()))
                .andExpect(jsonPath("$.productImageUrl").value(post2.getProductImageUrl().toString()))
                .andExpect(jsonPath("$.goodReason").value(post2.getGoodReason()))
                .andExpect(jsonPath("$.badReason").value(post2.getBadReason()));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void delete_post() throws Exception {
        BuyOrNotDto created = buyOrNotService.createBuyOrNot(ROOT_USER, post1);

        mockMvc.perform(delete("/api/v1/buyOrNots/{postId}", created.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/buyOrNots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSize").value(0));
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postCount").value(0));
    }

    @Test
    @WithMockUser(ROOT_USER)
    void vote_buy_or_not() throws Exception {
        BuyOrNotDto created = buyOrNotService.createBuyOrNot(ROOT_USER, post1);
        BuyOrNotVoteDto request = new BuyOrNotVoteDto();
        request.setVote(BuyOrNotVoteType.BAD);
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/buyOrNots/{postId}/vote", created.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodVoteCount").value(0))
                .andExpect(jsonPath("$.badVoteCount").value(1));
        mockMvc.perform(get("/api/v1/buyOrNots/{podtId}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goodVoteCount").value(0))
                .andExpect(jsonPath("$.badVoteCount").value(1));
    }

    @Test
    @WithMockUser(NORMAL_USER)
    void report_post() throws Exception {
        BuyOrNotDto created = buyOrNotService.createBuyOrNot(ROOT_USER, post1);
        BuyOrNotReportRequest request = new BuyOrNotReportRequest();
        request.setReason("잘못된 정보");
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/buyOrNots/{postId}/report", created.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(NORMAL_USER)
    void get_filtered_result() throws Exception {
        BuyOrNotDto created = buyOrNotService.createBuyOrNot(ROOT_USER, post1);
        BuyOrNotReportRequest request = new BuyOrNotReportRequest();
        request.setReason("잘못된 정보");
        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/buyOrNots/{postId}/report", created.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/buyOrNots"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalSize").value(0));
    }

}
