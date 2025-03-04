package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.goolbitg.api.v1.exception.AuthException;
import com.goolbitg.api.v1.exception.CommonException;

/**
 * EtcIntegrationTest
 */
@CustomIntegrationTest
public class EtcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCommonException() throws Exception {
        CommonException ex = AuthException.tokenNotExist();
        mockMvc.perform(get("/api/v1/health"))
            .andExpect(status().isUnauthorized());
            //.andExpect(jsonPath("$.code").value(ex.getCode()))
            //.andExpect(jsonPath("$.message").value(ex.getMessage()));
    }

}
