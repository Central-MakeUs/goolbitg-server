package com.goolbitg.api.controller;

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
import com.goolbitg.api.model.UserChecklistDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.model.UserHabitDto;
import com.goolbitg.api.model.UserInfoDto;
import com.goolbitg.api.model.UserPatternDto;
import com.goolbitg.api.security.AuthUtil;
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

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<UserDto> getMyInfo() throws Exception {
        UserDto dto = userService.getUser(AuthUtil.getLoginUserId());
        log.info("User DTO: " + dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NicknameCheckResponseDto> checkNickname(
            @Valid NicknameCheckRequestDto nicknameCheckRequestDto) throws Exception {
        NicknameCheckResponseDto response = userService.isNicknameExist(nicknameCheckRequestDto);
        log.info("Nickname Check: " + response);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> postChecklistInfo(@Valid UserChecklistDto userChecklistDto) throws Exception {
        userService.updateChecklistInfo(AuthUtil.getLoginUserId(), userChecklistDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postHabitInfo(@Valid UserHabitDto userHabitDto) throws Exception {
        userService.updateHabitinfo(AuthUtil.getLoginUserId(), userHabitDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postPatternInfo(@Valid UserPatternDto userPatternDto) throws Exception {
        userService.updatePatternInfo(AuthUtil.getLoginUserId(), userPatternDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> postUserInfo(@Valid UserInfoDto userInfoDto) throws Exception {
        userService.updateUserInfo(AuthUtil.getLoginUserId(), userInfoDto);
        return ResponseEntity.ok().build();
    }

}
