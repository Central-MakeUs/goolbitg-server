package com.goolbitg.api.v1.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.goolbitg.api.v1.exception.AuthException;
import com.goolbitg.api.v1.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.v1.entity.UnregisterHistory;
import com.goolbitg.api.v1.entity.User;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UnregisterDto;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.repository.RegistrationTokenRepository;
import com.goolbitg.api.v1.repository.UnregisterHistoryRepository;
import com.goolbitg.api.v1.repository.UserRepository;
import com.goolbitg.api.v1.repository.UserTokenRepository;
import com.goolbitg.api.v1.security.AppleLoginManager;
import com.goolbitg.api.v1.security.JwtManager;
import com.goolbitg.api.v1.util.RandomIdGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * AuthServiceImpl
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository tokenRepository;
    @Autowired
    private UnregisterHistoryRepository unregisterHistoryRepository;
    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;
    @Autowired
    private ChallengeRecordRepository challengeRecordRepository;
    @Autowired
    private JwtManager jwtManager;
    @Autowired
    private AppleLoginManager appleLoginManager;
    @Autowired
    private TimeService timeService;



    /* ------------ API Implements ------------ */

    @Override
    @Transactional
    public AuthResponseDto login(AuthRequestDto request) {
        Jwt jwt = extractToken(request);
        Optional<User> result = findUser(jwt, request);

        if (result.isEmpty()) {
            throw UserException.userNotExist(jwt.getSubject());
        }
        User user = result.get();

        String accessToken = jwtManager.create(user.getId(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String refreshToken = createRefreshToken(user.getId());

        AuthResponseDto dto = new AuthResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);

        return dto;
    }

    @Override
    @Transactional
    public void register(AuthRequestDto request) {
        Jwt jwt = extractToken(request);
        Optional<User> result = findUser(jwt, request);

        if (result.isPresent()) {
            throw UserException.alreadyRegistered(result.get().getId());
        }

        String userId = generateUserId();

        String kakaoId = null;
        String appleId = null;
        if (request.getType() == LoginType.KAKAO) {
            kakaoId = jwt.getSubject();
        } else {
            appleId = jwt.getSubject();
        }

        User user = User.builder()
                .id(userId)
                .registerDate(timeService.getToday())
                .kakaoId(kakaoId)
                .appleId(appleId)
                .build();
        userRepository.save(user);

        log.info("New User Created - " + userId);
    }

    @Override
    public AuthResponseDto getAccessToken(TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();
        Optional<String> result = tokenRepository.findUserIdByRefreshToken(refreshToken);

        if (result.isEmpty()) {
            throw AuthException.tokenExpired(refreshToken);
        }
        String userId = result.get();
        String accessToken = jwtManager.create(userId);

        AuthResponseDto dto = new AuthResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return dto;
    }

    @Override
    public void logout(String userId) {
        tokenRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void unregister(String userId, UnregisterDto request, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));

        if (user.getAppleId() != null) {
            if (request.getAuthorizationCode() == null)
                throw AuthException.authorizationCodeNotExist();

            try {
                appleLoginManager.unregisterUser(request.getAuthorizationCode());
            } catch (Exception e) {
                e.printStackTrace();
                throw AuthException.appleLoginAuthError(e.getMessage());
            }
        }

        UnregisterHistory history = UnregisterHistory.builder()
                .userId(userId)
                .reason(request.getReason())
                .unregisterDate(date)
                .build();

        unregisterHistoryRepository.save(history);
        userRepository.deleteById(userId);
        challengeRecordRepository.deleteByUserId(userId);

        log.info("User Unregisterd - " + userId);
    }

    /* ------------- DTO Mappers ------------ */

    /* ------------ Helper Methods ---------- */

    private String createRefreshToken(String userId) {
        String token = RandomHolder.randomKey(128);
        tokenRepository.save(userId, token);
        return token;
    }

    private static class RandomHolder {
        static final Random random = new SecureRandom();
        public static String randomKey(int length) {
            return String.format("%"+length+"s", new BigInteger(length*5, random)
                    .toString(32)).replace('\u0020', '0');
        }
    }

    private Jwt extractToken(AuthRequestDto request) {
        String jwkUri;
        if (request.getType() == LoginType.KAKAO) {
            jwkUri = "https://kauth.kakao.com/.well-known/jwks.json";
        } else {
            jwkUri = "https://appleid.apple.com/auth/keys";
        }
        JwtDecoder decoder = NimbusJwtDecoder
            .withJwkSetUri(jwkUri)
            .build();
        Jwt jwt = decoder.decode(request.getIdToken());
        return jwt;
    }

    private Optional<User> findUser(Jwt jwt, AuthRequestDto request) {
        String id = jwt.getSubject();
        Optional<User> result;
        if (request.getType() == LoginType.KAKAO) {
            result = userRepository.findByKakaoId(id);
        } else {
            result = userRepository.findByAppleId(id);
        }
        return result;
    }

    private String generateUserId() {
        String userId = RandomIdGenerator.generate();
        while (userRepository.existsById(userId)) {
            userId = RandomIdGenerator.generate();
        }
        return userId;
    }
}

