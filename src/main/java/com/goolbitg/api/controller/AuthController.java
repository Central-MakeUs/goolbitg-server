package com.goolbitg.api.controller;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.AuthApi;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.security.AuthUtil;
import com.goolbitg.api.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthController
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    @Autowired
    private final UserService userService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<AuthResponseDto> login(@Valid AuthRequestDto authRequestDto) throws Exception {
        return ResponseEntity.ok(userService.login(authRequestDto));
    }

    @Override
    public ResponseEntity<Void> register(@Valid AuthRequestDto authRequestDto) throws Exception {
        userService.register(authRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> unregister() throws Exception {
        userService.unregister(AuthUtil.getLoginUserId());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> logout() throws Exception {
        String userId = AuthUtil.getLoginUserId();
        userService.logout(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AuthResponseDto> refresh(@Valid TokenRefreshRequestDto tokenRefreshRequestDto)
            throws Exception {
        AuthResponseDto response = userService.getAccessToken(tokenRefreshRequestDto);
        log.info("AuthResponse: " + response);
        return ResponseEntity.ok(response);
    }

}
