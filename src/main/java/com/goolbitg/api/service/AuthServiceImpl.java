package com.goolbitg.api.service;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.RegistrationToken;
import com.goolbitg.api.entity.SpendingType;
import com.goolbitg.api.entity.UnregisterHistory;
import com.goolbitg.api.entity.User;
import com.goolbitg.api.entity.UserStat;
import com.goolbitg.api.entity.UserSurvey;
import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.model.SpendingTypeDto;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UnregisterDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.repository.ChallengeRecordRepository;
import com.goolbitg.api.repository.RegistrationTokenRepository;
import com.goolbitg.api.repository.SpendingTypeRepository;
import com.goolbitg.api.repository.UnregisterHistoryRepository;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.repository.UserStatRepository;
import com.goolbitg.api.repository.UserSurveyRepository;
import com.goolbitg.api.repository.UserTokenRepository;
import com.goolbitg.api.security.AppleLoginManager;
import com.goolbitg.api.security.JwtManager;
import com.goolbitg.api.util.FormatUtil;
import com.goolbitg.api.util.RandomIdGenerator;

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
    private UserSurveyRepository userSurveyRepository;
    @Autowired
    private UserStatRepository userStatsRepository;
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

        if (request.getRegistrationToken() != null) {
            RegistrationToken registrationToken = RegistrationToken.builder()
                .registrationToken(request.getRegistrationToken())
                .userId(user.getId())
                .build();

            registrationTokenRepository.save(registrationToken);
        }

        String accessToken = jwtManager.create(user.getId());
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

        User.UserBuilder userBuilder = User.builder()
                .id(userId)
                .registerDate(timeService.getToday());
        if (request.getType() == LoginType.KAKAO) {
            userBuilder.kakaoId(jwt.getSubject());
        } else {
            userBuilder.appleId(jwt.getSubject());
        }
        userRepository.save(userBuilder.build());

        UserSurvey survey = UserSurvey.getDefault(userId);
        userSurveyRepository.save(survey);

        UserStat stat = UserStat.getDefault(userId);
        userStatsRepository.save(stat);

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
        userSurveyRepository.deleteById(userId);
        userStatsRepository.deleteById(userId);
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

