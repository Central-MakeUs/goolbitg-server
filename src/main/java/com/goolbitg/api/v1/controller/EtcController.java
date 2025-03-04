package com.goolbitg.api.v1.controller;

import java.util.Optional;

import com.goolbitg.api.v1.repository.UserTokenRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.goolbitg.api.EtcApi;
import com.goolbitg.api.v1.data.CronJobExecutor;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.ImageUploadResponse;
import com.goolbitg.api.v1.security.JwtManager;
import com.goolbitg.api.v1.service.AdminService;
import com.goolbitg.api.v1.service.ImageService;

/**
 * ImageController
 */
@RestController
public class EtcController implements EtcApi {

    @Autowired
    private ImageService ImageService;
    @Autowired
    private JwtManager jwtManager;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CronJobExecutor cronJobExecutor;

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
    public ResponseEntity<Void> sendChallengeNotice(@NotNull @Valid String password) throws Exception {
        adminService.authenticateAdmin(password);
        cronJobExecutor.sendChallengeAlarm();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ImageUploadResponse> uploadImage(MultipartFile image) throws Exception {
        ImageUploadResponse result = ImageService.uploadImage(image);
        return ResponseEntity.ok(result);
    }

}
