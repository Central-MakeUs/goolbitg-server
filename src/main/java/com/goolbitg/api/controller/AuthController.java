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
import com.goolbitg.api.model.LoginResponseDto;
import com.goolbitg.api.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * AuthController
 */
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
    public ResponseEntity<LoginResponseDto> login(@Valid AuthRequestDto authRequestDto) throws Exception {
        return ResponseEntity.ok(userService.login(authRequestDto));
    }

    @Override
    public ResponseEntity<Void> register(@Valid AuthRequestDto authRequestDto) throws Exception {
        userService.register(authRequestDto);
        return ResponseEntity.ok().build();
    }

}
