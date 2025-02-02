package com.goolbitg.api.service;

import java.time.LocalDate;

import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.NicknameCheckRequestDto;
import com.goolbitg.api.model.NicknameCheckResponseDto;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UnregisterDto;
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

    UserDto getUser(String userId) throws Exception;
    void register(AuthRequestDto request);
    void unregister(String userId, UnregisterDto request, LocalDate date);
    AuthResponseDto login(AuthRequestDto request);
    AuthResponseDto getAccessToken(TokenRefreshRequestDto request);
    void logout(String userId);
    NicknameCheckResponseDto isNicknameExist(NicknameCheckRequestDto nickname);

    UserRegisterStatusDto getRegisterStatus(String userId);
    void updateAgreementInfo(String userId, UserAgreementDto request);
    void updateUserInfo(String userId, UserInfoDto request);
    void updateChecklistInfo(String userId, UserChecklistDto request);
    void updateHabitinfo(String userId, UserHabitDto request);
    void updatePatternInfo(String userId, UserPatternDto request);
    void postPushNotificationAgreement(String userId);
    UserWeeklyStatusDto getWeeklyStatus(String userId, LocalDate date);

}
