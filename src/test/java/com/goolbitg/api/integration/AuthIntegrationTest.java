package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.v1.service.AuthService;
import com.goolbitg.api.v1.service.BuyOrNotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

@CustomIntegrationTest
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AuthService authService;

    private AuthRequestDto auth;

    @BeforeEach
    void preparePost() {
        auth = new AuthRequestDto();
        auth.setType(LoginType.TEST);
        auth.setIdToken("TEST");
    }

    @Test
    void register_a_test_user() throws Exception {
        String body = mapper.writeValueAsString(auth);
        mockMvc.perform(post("/api/v1/auth/register")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void login_a_test_user() throws Exception {
        String body = mapper.writeValueAsString(auth);
        authService.register(auth);
        mockMvc.perform(post("/api/v1/auth/login")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
