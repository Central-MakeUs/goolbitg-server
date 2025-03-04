package com.goolbitg.api.v1.service;

import java.time.LocalDate;

import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UnregisterDto;
import com.goolbitg.api.model.UserDto;

/**
 * AuthService
 */
public interface AuthService {

    void register(AuthRequestDto request);
    void unregister(String userId, UnregisterDto request, LocalDate date);
    AuthResponseDto login(AuthRequestDto request);
    AuthResponseDto getAccessToken(TokenRefreshRequestDto request);
    void logout(String userId);

}
