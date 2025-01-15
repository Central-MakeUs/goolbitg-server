package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.goolbitg.api.controllers.EtcController;
import com.goolbitg.api.exceptions.AuthException;
import com.goolbitg.api.exceptions.CommonException;

/**
 * EtcIntegrationTest
 */
@WebMvcTest(EtcController.class)
public class EtcIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCommonException() throws Exception {
        CommonException ex = AuthException.tokenNotExist();
        mockMvc.perform(get("/health"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(ex.getCode()))
            .andExpect(jsonPath("$.message").value(ex.getMessage()));
    }

}
