package com.goolbitg.api.service;

import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.LoginResponseDto;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UserDto;

/**
 * UserService
 */
public interface UserService {

    UserDto getUser(String id) throws Exception;
    void register(AuthRequestDto request);
    LoginResponseDto login(AuthRequestDto request);
    AuthResponseDto getAccessToken(TokenRefreshRequestDto request);
    void logout(String userId);

}
