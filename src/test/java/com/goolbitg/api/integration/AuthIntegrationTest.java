package com.goolbitg.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.v1.security.JwtManager;
import com.goolbitg.api.v1.service.AuthService;

@CustomIntegrationTest
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtManager jwtManager;

    private AuthRequestDto auth;
    private final String INVALID_TOKEN = "INVALID_TOKEN";

    @BeforeEach
    void preparePost() {
        auth = new AuthRequestDto();
        auth.setType(LoginType.TEST);
        auth.setIdToken("TEST");
    }

    @Test
    void register_test_user() throws Exception {
        String body = mapper.writeValueAsString(auth);
        mockMvc.perform(post("/api/v1/auth/register")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void login_test_user() throws Exception {
        String body = mapper.writeValueAsString(auth);
        authService.register(auth);
        mockMvc.perform(post("/api/v1/auth/login")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void logout_test_user() throws Exception {
        String body = mapper.writeValueAsString(auth);
        authService.register(auth);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        try (JsonParser parser = mapper.createParser(response.getContentAsString())) {
            AuthResponseDto authResponseDto = parser.readValueAs(AuthResponseDto.class);
            assertThat(authResponseDto.getAccessToken()).isNotNull();

            mockMvc.perform(post("/api/v1/auth/logout")
                    .header("Authorization", "Bearer " + authResponseDto.getAccessToken()))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @MethodSource("testEndpoints")
    @Disabled
    void logout_without_token(String url, HttpMethod method) throws Exception {
        authService.register(auth);
        authService.login(auth);
        performEach(url, method)
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.code").value(2003))
            .andExpect(jsonPath("$.message").value(containsString("인증 정보가 없습니다.")));
    }

    @ParameterizedTest
    @MethodSource("testEndpoints")
    @Disabled
    void logout_with_expired_token(String url, HttpMethod method) throws Exception {
        authService.register(auth);
        authService.login(auth);
        String expiredToken = jwtManager.createExpired("TEST");
        performEach(url, method, Map.of("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(2002))
                .andExpect(jsonPath("$.message").value(containsString("토큰이 만료되었습니다.")));
    }

    @ParameterizedTest
    @MethodSource("testEndpoints")
    @Disabled
    void logout_with_invalid_token(String url, HttpMethod method) throws Exception {
        authService.register(auth);
        authService.login(auth);
        performEach(url, method, Map.of("Authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(2001))
                .andExpect(jsonPath("$.message").value(containsString("토큰이 유효하지 않습니다.")));
    }

    ResultActions performEach(String url, HttpMethod method) throws Exception {
        return performEach(url, method, Map.of());
    }

    ResultActions performEach(String url,
                              HttpMethod method,
                              Map<String, Object> headers) throws Exception {
        MockHttpServletRequestBuilder httpBuilder;
        if (method.equals(HttpMethod.GET)) {
            httpBuilder = get(url);
        } else if (method.equals(HttpMethod.POST)) {
            httpBuilder = post(url);
        } else {
            throw new IllegalArgumentException("Invalid Method");
        }

        for (Map.Entry<String, Object> entries : headers.entrySet()) {
            httpBuilder.header(entries.getKey(), entries.getValue());
        }
        return mockMvc.perform(httpBuilder);
    }

    static Stream<Arguments> testEndpoints() {
        return Stream.of(
                Arguments.of("/api/v1/auth/logout", HttpMethod.POST),
                Arguments.of("/api/v1/users/me", HttpMethod.GET)
        );
    }

}
