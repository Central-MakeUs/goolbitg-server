package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.goolbitg.api.controllers.UserController;

/**
 * UserIntegrationTest
 */
@WebMvcTest(UserController.class)

public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void controllerShouldReturnContent() throws Exception {
        mockMvc.perform(get("/users/me").header("Accept", "application/json"))
               .andExpect(status().isNotImplemented())
               .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.isEmptyString())));
    }
}
