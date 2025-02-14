package com.goolbitg.api.controller;

import java.time.LocalDate;
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
import com.goolbitg.api.model.UnregisterDto;
import com.goolbitg.api.security.AuthUtil;
import com.goolbitg.api.service.AuthService;
import com.goolbitg.api.service.TimeService;

import lombok.extern.slf4j.Slf4j;

/**
 * AuthController
 */
@Slf4j
@RestController
public class AuthController implements AuthApi {

    @Autowired
    private AuthService authService;
    @Autowired
    private TimeService timeService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<AuthResponseDto> login(@Valid AuthRequestDto authRequestDto) throws Exception {
        return ResponseEntity.ok(authService.login(authRequestDto));
    }

    @Override
    public ResponseEntity<Void> register(@Valid AuthRequestDto authRequestDto) throws Exception {
        authService.register(authRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> logout() throws Exception {
        String userId = AuthUtil.getLoginUserId();
        authService.logout(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AuthResponseDto> refresh(@Valid TokenRefreshRequestDto tokenRefreshRequestDto)
            throws Exception {
        AuthResponseDto response = authService.getAccessToken(tokenRefreshRequestDto);
        log.info("AuthResponse: " + response);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> unregister(@Valid UnregisterDto unregisterDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        LocalDate date = timeService.getToday();

        authService.unregister(userId, unregisterDto, date);
        return ResponseEntity.ok().build();
    }

}
