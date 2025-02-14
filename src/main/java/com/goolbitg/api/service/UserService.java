package com.goolbitg.api.service;

import java.time.LocalDate;

import com.goolbitg.api.entity.UserSurvey;
import com.goolbitg.api.model.NicknameCheckRequestDto;
import com.goolbitg.api.model.NicknameCheckResponseDto;
import com.goolbitg.api.model.UserAgreementDto;
import com.goolbitg.api.model.UserChecklistDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.model.UserHabitDto;
import com.goolbitg.api.model.UserInfoDto;
import com.goolbitg.api.model.UserPatternDto;
import com.goolbitg.api.model.UserRegisterStatusDto;
import com.goolbitg.api.model.UserWeeklyStatusDto;

/**
 * UserService
 */
public interface UserService {

    UserDto getUser(String userId);
    UserRegisterStatusDto getRegisterStatus(String userId);
    void updateAgreementInfo(String userId, UserAgreementDto request);
    void updateUserInfo(String userId, UserInfoDto request);
    void updateChecklistInfo(String userId, UserChecklistDto request);
    void updateHabitinfo(String userId, UserHabitDto request);
    void updatePatternInfo(String userId, UserPatternDto request);
    void postPushNotificationAgreement(String userId);
    UserWeeklyStatusDto getWeeklyStatus(String userId, LocalDate date);
    void updateUserStat(String userId, LocalDate date);
    Long determineSpendingType(UserSurvey survey);
    NicknameCheckResponseDto isNicknameExist(NicknameCheckRequestDto nickname);

}
