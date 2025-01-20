package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * TestIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(value = "id0001")
    void test() throws Exception {
        mockMvc.perform(get("/test"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("Test OK"));
    }

}
