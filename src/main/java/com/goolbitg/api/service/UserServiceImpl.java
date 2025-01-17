package com.goolbitg.api.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.User;
import com.goolbitg.api.entity.UserStats;
import com.goolbitg.api.entity.UserSurvey;
import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.LoginResponseDto;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.repository.UserStatsRepository;
import com.goolbitg.api.repository.UserSurveyRepository;
import com.goolbitg.api.repository.UserTokenRepository;
import com.goolbitg.api.security.AuthUtil;
import com.goolbitg.api.security.JwtManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserSurveyRepository userSurveyRepository;
    @Autowired
    private final UserStatsRepository userStatsRepository;
    @Autowired
    private final UserTokenRepository tokenRepository;
    @Autowired
    private final JwtManager jwtManager;

    private int idSeq = 2;

    @Override
    public UserDto getUser(String id) throws Exception {
        log.info("User id: " + id);
        User user = userRepository.findById(id)
            .orElseThrow(() -> UserException.userNotExist(id));
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setNickname(user.getNickname());
        dto.setBirthday(user.getBirthday());
        dto.setGender(user.getGender());

        return dto;
    }

    @Override
    @Transactional
    public LoginResponseDto login(AuthRequestDto request) {
        Jwt jwt = extractToken(request);
        Optional<User> result = findUser(jwt, request);

        if (result.isEmpty()) {
            throw UserException.userNotExist(jwt.getSubject());
        }
        User user = result.get();

        UserDetails details = AuthUtil.createUserDetails(user.getId());

        String accessToken = jwtManager.create(details);
        String refreshToken = createRefreshToken(user.getId());

        LoginResponseDto dto = new LoginResponseDto();
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

        User user = new User();
        String userId = String.format("id%04d", idSeq++);
        user.setId(userId);
        if (request.getType() == LoginType.KAKAO) {
            user.setKakaoId(jwt.getSubject());
        } else {
            user.setAppleId(jwt.getSubject());
        }
        user.setRegisterDate(LocalDate.now());
        userRepository.save(user);

        UserSurvey survey = new UserSurvey();
        survey.setUserId(userId);
        userSurveyRepository.save(survey);

        UserStats stats = new UserStats();
        stats.setUserId(userId);
        userStatsRepository.save(stats);
    }

    @Override
    public AuthResponseDto getAccessToken(TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();
        Optional<String> result = tokenRepository.findUserIdByRefreshToken(refreshToken);

        if (result.isEmpty()) {
            throw AuthException.tokenExpired(refreshToken);
        }
        String userId = result.get();
        UserDetails details = AuthUtil.createUserDetails(userId);
        String accessToken = jwtManager.create(details);

        AuthResponseDto dto = new AuthResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return dto;
    }

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

    @Override
    public void logout(String userId) {
        tokenRepository.deleteByUserId(userId);
    }
}
