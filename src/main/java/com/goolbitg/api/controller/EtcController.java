package com.goolbitg.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.EtcApi;
import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.model.LoginResponseDto;
import com.goolbitg.api.repository.UserTokenRepository;
import com.goolbitg.api.security.JwtManager;

import lombok.RequiredArgsConstructor;

/**
 * ImageController
 */
@RestController
@RequiredArgsConstructor
public class EtcController implements EtcApi {

    @Autowired
    private final JwtManager jwtManager;
    @Autowired
    private final UserTokenRepository userTokenRepository;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<Void> checkHealth() throws Exception {
        // NOTE: for test
        throw AuthException.tokenNotExist();
    }

    @Override
    public ResponseEntity<LoginResponseDto> getRootAccess() throws Exception {
        String accessToken = jwtManager.createPermanent("id0001");
        String refreshToken = "root_user_refresh_token";
        userTokenRepository.save("id0001", "root_user_refresh_token");
        LoginResponseDto dto = new LoginResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return ResponseEntity.ok(dto);
    }

}
