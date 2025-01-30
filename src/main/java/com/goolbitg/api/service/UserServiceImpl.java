package com.goolbitg.api.service;

import java.math.BigInteger;
import java.net.URI;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.DailyRecord;
import com.goolbitg.api.entity.SpendingType;
import com.goolbitg.api.entity.User;
import com.goolbitg.api.entity.UserStats;
import com.goolbitg.api.entity.UserSurvey;
import com.goolbitg.api.exception.AuthException;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.AuthRequestDto;
import com.goolbitg.api.model.AuthResponseDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.LoginType;
import com.goolbitg.api.model.NicknameCheckRequestDto;
import com.goolbitg.api.model.NicknameCheckResponseDto;
import com.goolbitg.api.model.SpendingTypeDto;
import com.goolbitg.api.model.TokenRefreshRequestDto;
import com.goolbitg.api.model.UserAgreementDto;
import com.goolbitg.api.model.UserChecklistDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.model.UserHabitDto;
import com.goolbitg.api.model.UserInfoDto;
import com.goolbitg.api.model.UserPatternDto;
import com.goolbitg.api.model.UserRegisterStatusDto;
import com.goolbitg.api.model.UserWeeklyStatusDto;
import com.goolbitg.api.repository.DailyRecordRepository;
import com.goolbitg.api.repository.SpendingTypeRepository;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.repository.UserStatsRepository;
import com.goolbitg.api.repository.UserSurveyRepository;
import com.goolbitg.api.repository.UserTokenRepository;
import com.goolbitg.api.security.JwtManager;
import com.goolbitg.api.util.FormatUtil;

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
    private final SpendingTypeRepository spendingTypeRepository;
    @Autowired
    private final DailyRecordRepository dailyRecordRepository;
    @Autowired
    private final JwtManager jwtManager;
    @Autowired
    private final Clock clock;

    private int idSeq = 2;

    private final int CHICKEN_PRICE = 15000;


    /* --------------- API Implements ----------------------*/

    @Override
    public UserDto getUser(String userId) throws Exception {
        log.info("User id: " + userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));
        UserSurvey survey = userSurveyRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));
        UserStats stats = userStatsRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));

        return getUserDto(user, survey, stats);
    }

    @Override
    @Transactional
    public AuthResponseDto login(AuthRequestDto request) {
        Jwt jwt = extractToken(request);
        Optional<User> result = findUser(jwt, request);

        if (result.isEmpty()) {
            throw UserException.userNotExist(jwt.getSubject());
        }
        User user = result.get();

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
        stats.setPostCount(0);
        stats.setChallengeCount(0);
        stats.setAchivementGuage(0);
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
    public NicknameCheckResponseDto isNicknameExist(NicknameCheckRequestDto nickname) {
        NicknameCheckResponseDto dto = new NicknameCheckResponseDto();
        dto.setDuplicated(isNicknameExistInner(nickname.getNickname()));
        return dto;
    }

    @Override
    @Transactional
    public void updateUserInfo(String userId, UserInfoDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        if (user.getAgreement1() == null)
            throw UserException.previousStepNotComplete(0);

        if (isNicknameExistInner(request.getNickname())) {
            // TODO: Integrated validation error message required.
            throw new IllegalArgumentException(
                "이미 존재하는 닉네임입니다. (" + request.getNickname() + ")"
            );
        }

        user.setNickname(request.getNickname());
        user.setBirthday(request.getBirthday());
        user.setGender(request.getGender());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateChecklistInfo(String userId, UserChecklistDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        if (user.getNickname() == null)
            throw UserException.previousStepNotComplete(1);

        UserSurvey survey = userSurveyRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));

        survey.setCheck1(request.getCheck1());
        survey.setCheck2(request.getCheck2());
        survey.setCheck3(request.getCheck3());
        survey.setCheck4(request.getCheck4());
        survey.setCheck5(request.getCheck5());
        survey.setCheck6(request.getCheck6());
        userSurveyRepository.save(survey);
    }

    @Override
    @Transactional
    public void updateHabitinfo(String userId, UserHabitDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        if (user.getNickname() == null)
            throw UserException.previousStepNotComplete(1);

        UserSurvey survey = userSurveyRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));

        survey.setAvgIncomePerMonth(request.getAvgIncomePerMonth());
        survey.setAvgSpendingPerMonth(request.getAvgSpendingPerMonth());
        user.setSpendingTypeId(determineSpendingType(survey));
        userSurveyRepository.save(survey);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePatternInfo(String userId, UserPatternDto request) {
        Optional<UserSurvey> result = userSurveyRepository.findById(userId);
        if (result.isEmpty()) {
            throw UserException.userNotExist(userId);
        }

        UserSurvey survey = result.get();
        survey.setPrimeUseDay(request.getPrimeUseDay());

        survey.setPrimeUseTime(FormatUtil.parseTime(request.getPrimeUseTime()));
        userSurveyRepository.save(survey);
    }

    @Override
    @Transactional
    public void unregister(String userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw UserException.userNotExist(userId);
        }
        userSurveyRepository.deleteById(userId);
        userStatsRepository.deleteById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserRegisterStatusDto getRegisterStatus(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));
        UserSurvey survey = userSurveyRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));
        int status = getRegisterStatusInner(user, survey);
        UserRegisterStatusDto dto = new UserRegisterStatusDto();
        dto.setStatus(status);
        dto.setRequiredInfoCompleted(status >= 4);
        return dto;
    }

    @Override
    @Transactional
    public void updateAgreementInfo(String userId, UserAgreementDto request) {
        // TODO: throw error when required agreement is not agreed
        Optional<User> result = userRepository.findById(userId);
        if (result.isEmpty()) {
            throw UserException.userNotExist(userId);
        }

        User user = result.get();
        user.setAgreement1(request.getAgreement1());
        user.setAgreement2(request.getAgreement2());
        user.setAgreement3(request.getAgreement3());
        user.setAgreement4(request.getAgreement4());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void postPushNotificationAgreement(String userId) {
        Optional<User> result = userRepository.findById(userId);
        if (result.isEmpty()) {
            throw UserException.userNotExist(userId);
        }

        User user = result.get();
        user.setAllowPushNotification(true);
        userRepository.save(user);
    }

    @Override
    public UserWeeklyStatusDto getWeeklyStatus(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        UserStats stats = userStatsRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        LocalDate today = getToday();
        DayOfWeek todayOfWeek = today.getDayOfWeek();
        int todayIndex = todayOfWeek.getValue() - 1;
        LocalDate sunday = today.minusDays(todayIndex + 1);
        List<DailyRecord> records = dailyRecordRepository.findByUserIdAndDateBetween(userId, sunday, today);

        return getWeeklyStatusDto(user, stats, todayIndex, records);
    }


    /* ------------ DTO Mappers ------------- */

    private UserDto getUserDto(User user, UserSurvey survey, UserStats stats) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setBirthday(user.getBirthday());
        dto.setGender(user.getGender());
        dto.setCheck1(survey.getCheck1());
        dto.setCheck2(survey.getCheck2());
        dto.setCheck3(survey.getCheck3());
        dto.setCheck4(survey.getCheck4());
        dto.setCheck5(survey.getCheck5());
        dto.setCheck6(survey.getCheck6());
        dto.setAvgIncomePerMonth(survey.getAvgIncomePerMonth());
        dto.setAvgSpendingPerMonth(survey.getAvgSpendingPerMonth());
        dto.setSpendingHabitScore(survey.getSpendingHabitScore());
        dto.setPostCount(stats.getPostCount());
        dto.setChallengeCount(stats.getChallengeCount());
        dto.setAchivementGuage(stats.getAchivementGuage());

        if (user.getSpendingTypeId() != null) {
            SpendingType spendingType = 
                spendingTypeRepository.findById(user.getSpendingTypeId()).get();
            Integer peopleCount = userRepository.countBySpendingTypeId(user.getSpendingTypeId());
            SpendingTypeDto spendingTypeDto = new SpendingTypeDto();
            spendingTypeDto.setId(spendingType.getId());
            spendingTypeDto.setTitle(spendingType.getTitle());
            spendingTypeDto.setImageUrl(URI.create(spendingType.getImageUrl()));
            spendingTypeDto.setPeopleCount(peopleCount);
            spendingTypeDto.setGoal(spendingType.getGoal());
            dto.setSpendingType(spendingTypeDto);
        }

        if (survey.getPrimeUseDay() != null)
            dto.setPrimeUseDay(survey.getPrimeUseDay().getValue());
        dto.setPrimeUseTime(FormatUtil.formatTime(survey.getPrimeUseTime()));

        return dto;
    }

    private UserWeeklyStatusDto getWeeklyStatusDto(User user, UserStats stats, int todayIndex, List<DailyRecord> records) {
        int saving = 0;
        UserWeeklyStatusDto dto = new UserWeeklyStatusDto();
        List<ChallengeRecordStatus> weekStatus = new ArrayList<>();
        for (DailyRecord record : records) {
            if (record == null) {
                weekStatus.add(ChallengeRecordStatus.FAIL);
            } else {
                weekStatus.add(record.getStatus());
                saving += record.getSaving();
            }
        }
        dto.setNickname(user.getNickname());
        dto.setContinueCount(stats.getContinueCount());
        dto.setSaving(saving);
        dto.setChickenCount((int)(saving / CHICKEN_PRICE));
        dto.setTodayIndex(todayIndex);
        dto.setWeekStatus(weekStatus);
        return dto;
    }


    /* ------------- Helper Methods ------------- */

    private int getRegisterStatusInner(User user, UserSurvey survey) {
        if (user.getAgreement1() == null) return 0;
        if (user.getNickname() == null) return 1;
        if (survey.getCheck1() == null) return 2;
        if (survey.getAvgIncomePerMonth() == null) return 3;
        if (survey.getPrimeUseDay() == null) return 4;
        return 5;
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

    private boolean isNicknameExistInner(String nickname) {
        Optional<User> result = userRepository.findByNickname(nickname);
        return result.isPresent();
    }

    private Long determineSpendingType(UserSurvey survey) {
        Integer checklistScore = survey.getChecklistScore();
        Integer spendingHabitScore = survey.getSpendingHabitScore();
        if (checklistScore == null || spendingHabitScore == null)
            throw UserException.registrationNotComplete(survey.getUserId());

        if (checklistScore == 3 || spendingHabitScore < 50)
            return 1L;
        if (checklistScore == 2 || spendingHabitScore < 70)
            return 2L;
        if (checklistScore == 1 || spendingHabitScore < 80)
            return 3L;
        if (checklistScore == 0 || spendingHabitScore < 90)
            return 4L;

        return 5L;
    }

    private LocalDate getToday() {
        return LocalDate.now(clock);
    }

}
