package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.Day;
import com.goolbitg.api.model.Gender;
import com.goolbitg.api.model.NicknameCheckRequestDto;
import com.goolbitg.api.model.UserAgreementDto;
import com.goolbitg.api.model.UserChecklistDto;
import com.goolbitg.api.model.UserHabitDto;
import com.goolbitg.api.model.UserInfoDto;
import com.goolbitg.api.model.UserPatternDto;

/**
 * UserIntegrationTest
 */
@CustomIntegrationTest
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private  ObjectMapper mapper;

    private final String ROOT_USER_ID = "id0001";
    private final String NEW_USER_ID = "id0002";

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void get_user_info() throws Exception {
        mockMvc.perform(get("/users/me").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value("굴비왕"))
            .andExpect(jsonPath("$.spendingType.id").value(5))
            .andExpect(jsonPath("$.spendingType.peopleCount").value(2))
            .andExpect(jsonPath("$.postCount").value(1));
    }

    @Test
    @Transactional
    @WithMockUser(ROOT_USER_ID)
    void check_nickname_duplication() throws Exception {
        NicknameCheckRequestDto requestBody = new NicknameCheckRequestDto();
        requestBody.setNickname("굴비왕");

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/nickname/check")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duplicated").value(true));
    }

    @Test
    @Transactional
    @WithMockUser(NEW_USER_ID)
    void cannot_update_duplicated_nickname() throws Exception {
        updateAgreement();
        UserInfoDto requestBody = new UserInfoDto();
        requestBody.setNickname("굴비왕");
        requestBody.setBirthday(LocalDate.parse("1999-03-01"));
        requestBody.setGender(Gender.FEMALE);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/info")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    @WithMockUser(NEW_USER_ID)
    void update_info_and_check_status() throws Exception {
        updateAgreement();
        mockMvc.perform(get("/users/me/registerStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.requiredInfoCompleted").value(false));

        updateUserInfo();
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value("굴비노예"))
            .andExpect(jsonPath("$.birthday").value("1999-03-01"))
            .andExpect(jsonPath("$.gender").value("FEMALE"));
        mockMvc.perform(get("/users/me/registerStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(2))
                .andExpect(jsonPath("$.requiredInfoCompleted").value(false));

        updateChecklist();
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.check1").value(true))
            .andExpect(jsonPath("$.check3").value(false));
        mockMvc.perform(get("/users/me/registerStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(3))
                .andExpect(jsonPath("$.requiredInfoCompleted").value(false));

        updateHabit();
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.avgIncomePerMonth").value(360000))
            .andExpect(jsonPath("$.avgSpendingPerMonth").value(200000))
            .andExpect(jsonPath("$.spendingType.id").value(1));
        mockMvc.perform(get("/users/me/registerStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(4))
                .andExpect(jsonPath("$.requiredInfoCompleted").value(true));

        updatePattern();
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.primeUseDay").value("SUNDAY"))
            .andExpect(jsonPath("$.primeUseTime").value("18:30:00"));
        mockMvc.perform(get("/users/me/registerStatus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(5))
                .andExpect(jsonPath("$.requiredInfoCompleted").value(true));
    }

    private void updateAgreement() throws JsonProcessingException, Exception {
        UserAgreementDto requestBody = new UserAgreementDto();
        requestBody.setAgreement1(true);
        requestBody.setAgreement2(true);
        requestBody.setAgreement3(true);
        requestBody.setAgreement4(false);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/agreement")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

    private void updateUserInfo() throws JsonProcessingException, Exception {
        UserInfoDto requestBody = new UserInfoDto();
        requestBody.setNickname("굴비노예");
        requestBody.setBirthday(LocalDate.parse("1999-03-01"));
        requestBody.setGender(Gender.FEMALE);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/info")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

    private void updateChecklist() throws JsonProcessingException, Exception {
        UserChecklistDto requestBody = new UserChecklistDto();
        requestBody.setCheck1(true);
        requestBody.setCheck2(true);
        requestBody.setCheck3(false);
        requestBody.setCheck4(false);
        requestBody.setCheck5(true);
        requestBody.setCheck6(false);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/checklist")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

    private void updateHabit() throws JsonProcessingException, Exception {
        UserHabitDto requestBody = new UserHabitDto();
        requestBody.setAvgIncomePerMonth(360000);
        requestBody.setAvgSpendingPerMonth(200000);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/habit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

    private void updatePattern() throws JsonProcessingException, Exception {
        UserPatternDto requestBody = new UserPatternDto();
        requestBody.setPrimeUseDay(Day.SUNDAY);
        requestBody.setPrimeUseTime("18:30:00");

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/pattern")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());
    }

}
