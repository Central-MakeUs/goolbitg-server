package com.goolbitg.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goolbitg.api.model.Day;
import com.goolbitg.api.model.Gender;
import com.goolbitg.api.model.NicknameCheckRequestDto;
import com.goolbitg.api.model.UserChecklistDto;
import com.goolbitg.api.model.UserHabitDto;
import com.goolbitg.api.model.UserInfoDto;
import com.goolbitg.api.model.UserPatternDto;

/**
 * UserIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private  ObjectMapper mapper;

    private final String ROOT_USER_ID = "id0001";
    private final String NEW_USER_ID = "id0002";

    @Test
    @WithMockUser(value = ROOT_USER_ID)
    void get_user_info() throws Exception {
        mockMvc.perform(get("/users/me").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").isString());
    }

    @Test
    @WithMockUser(value = ROOT_USER_ID)
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
    @WithMockUser(value = NEW_USER_ID)
    void update_user_info() throws Exception {
        UserInfoDto requestBody = new UserInfoDto();
        requestBody.setNickname("굴비노예");
        requestBody.setBirthday(LocalDate.parse("1999-03-01"));
        requestBody.setGender(Gender.FEMALE);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/info")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value("굴비노예"))
            .andExpect(jsonPath("$.birthday").value("1999-03-01"))
            .andExpect(jsonPath("$.gender").value("female"));
    }

    @Test
    @WithMockUser(value = NEW_USER_ID)
    void update_checklist() throws Exception {
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

        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.check1").value(true))
            .andExpect(jsonPath("$.check3").value(false));
    }

    @Test
    @WithMockUser(value = NEW_USER_ID)
    void update_habit() throws Exception {
        UserHabitDto requestBody = new UserHabitDto();
        requestBody.setAvgIncomePerMonth(360000);
        requestBody.setAvgSpendingPerMonth(200000);

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/habit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.avgIncomePerMonth").value(360000))
            .andExpect(jsonPath("$.avgSpendingPerMonth").value(200000));
    }

    @Test
    @WithMockUser(value = NEW_USER_ID)
    void update_pattern() throws Exception {
        UserPatternDto requestBody = new UserPatternDto();
        requestBody.setPrimeUseDay(Day.SUNDAY);
        requestBody.setPrimeUseTime("18:30:00");

        String jsonBody = mapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/users/me/pattern")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.primeUseDay").value("sunday"))
            .andExpect(jsonPath("$.primeUseTime").value("18:30:00"));
    }

}
