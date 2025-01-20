package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.exception.CommonException;

/**
 * EtcIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class EtcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCommonException() throws Exception {
        CommonException ex = AuthException.tokenNotExist();
        mockMvc.perform(get("/health"))
            .andExpect(status().isUnauthorized());
            //.andExpect(jsonPath("$.code").value(ex.getCode()))
            //.andExpect(jsonPath("$.message").value(ex.getMessage()));
    }

}
