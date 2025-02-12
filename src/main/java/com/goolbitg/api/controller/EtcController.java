package com.goolbitg.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.EtcApi;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.ImageUploadResponse;
import com.goolbitg.api.repository.UserTokenRepository;
import com.goolbitg.api.security.JwtManager;
import com.goolbitg.api.service.ImageService;

import lombok.RequiredArgsConstructor;

/**
 * ImageController
 */
@RestController
@RequiredArgsConstructor
public class EtcController implements EtcApi {

    @Autowired
    private final ImageService ImageService;

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
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AuthResponseDto> getRootAccess() throws Exception {
        String accessToken = jwtManager.createPermanent("id0001");
        String refreshToken = "root_user_refresh_token";
        userTokenRepository.save("id0001", "root_user_refresh_token");
        AuthResponseDto dto = new AuthResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<ImageUploadResponse> uploadImage(MultipartFile image) throws Exception {
        ImageUploadResponse result = ImageService.uploadImage(image);
        return ResponseEntity.ok(result);
    }

}
