package com.goolbitg.api.controller;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.UserApi;
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
import com.goolbitg.api.security.AuthUtil;
import com.goolbitg.api.service.TimeService;
import com.goolbitg.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserController
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    @Autowired
    private final UserService userService;
    @Autowired
    private final TimeService timeService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<UserDto> getMyInfo() throws Exception {
        UserDto dto = userService.getUser(AuthUtil.getLoginUserId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NicknameCheckResponseDto> checkNickname(
            @Valid NicknameCheckRequestDto nicknameCheckRequestDto) throws Exception {
        NicknameCheckResponseDto response = userService.isNicknameExist(nicknameCheckRequestDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> postChecklistInfo(@Valid UserChecklistDto userChecklistDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        userService.updateChecklistInfo(userId, userChecklistDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postHabitInfo(@Valid UserHabitDto userHabitDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        userService.updateHabitinfo(userId, userHabitDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postPatternInfo(@Valid UserPatternDto userPatternDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        userService.updatePatternInfo(userId, userPatternDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postUserInfo(@Valid UserInfoDto userInfoDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        userService.updateUserInfo(userId, userInfoDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserRegisterStatusDto> getRegisterStatus() throws Exception {
        String userId = AuthUtil.getLoginUserId();

        UserRegisterStatusDto result = userService.getRegisterStatus(userId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> postPushNotificationAgreement() throws Exception {
        String userId = AuthUtil.getLoginUserId();

        userService.postPushNotificationAgreement(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postUserAgreement(@Valid UserAgreementDto userAgreementDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        userService.updateAgreementInfo(userId, userAgreementDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserWeeklyStatusDto> getWeeklyStatus(@Valid LocalDate date) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        if (date == null)
            date = timeService.getToday();

        UserWeeklyStatusDto result = userService.getWeeklyStatus(userId, date);
        return ResponseEntity.ok(result);
    }

}
