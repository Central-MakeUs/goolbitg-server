package com.goolbitg.api.integration;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final String NORMAL_USER_ID = "id0002";

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void get_buyOrNots() throws Exception {

    }
}
