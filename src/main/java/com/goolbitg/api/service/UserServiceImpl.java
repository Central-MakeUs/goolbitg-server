package com.goolbitg.api.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.goolbitg.api.controller.UserController;
import com.goolbitg.api.entity.User;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.LoginResponseDto;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.repository.UserTokenRepository;
import com.goolbitg.api.security.JwtManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
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
    public LoginResponseDto login(AuthRequestDto request) {
        Jwt jwt = extractToken(request);
        Optional<User> result = findUser(jwt, request);


        log.info("User Found: " + result);
        if (result.isEmpty()) {
            throw UserException.userNotExist(jwt.getSubject());
        }
        User user = result.get();

        log.info("User exists: " + user);
        UserDetails details = org.springframework.security.core.userdetails.User
            .withUsername(user.getId())
            .password("")
            .authorities(List.of())
            .build();

        log.info("UserDetails created:" + details);
        String accessToken = jwtManager.create(details);
        String refreshToken = createRefreshToken(user.getId());

        log.info("token creation success: (" + accessToken + "," + refreshToken + ")");
        LoginResponseDto dto = new LoginResponseDto();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return dto;
    }

    @Override
    public void register(AuthRequestDto request) {
        Jwt jwt = extractToken(request);
        Optional<User> result = findUser(jwt, request);

        if (result.isPresent()) {
            throw UserException.alreadyRegistered(result.get().getId());
        }

        User user = new User();
        user.setId(String.format("id%04d", idSeq++));
        if (request.getType() == LoginType.KAKAO) {
            user.setKakaoId(jwt.getSubject());
        } else {
            user.setAppleId(jwt.getSubject());
        }
        user.setRegisterDate(LocalDate.now());
        userRepository.save(user);
    }

    @Override
    public AuthResponseDto getAccessToken(TokenRefreshRequestDto request) {
        return null;
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
}
