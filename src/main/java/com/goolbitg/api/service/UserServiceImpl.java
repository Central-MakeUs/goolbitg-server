package com.goolbitg.api.service;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.DailyRecord;
import com.goolbitg.api.entity.DailyRecordId;
import com.goolbitg.api.entity.RegistrationToken;
import com.goolbitg.api.entity.SpendingType;
import com.goolbitg.api.entity.User;
import com.goolbitg.api.entity.UserStat;
import com.goolbitg.api.entity.UserSurvey;
import com.goolbitg.api.exception.NoticeException;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.NicknameCheckRequestDto;
import com.goolbitg.api.model.NicknameCheckResponseDto;
import com.goolbitg.api.model.RegistrationTokenRegisterDto;
import com.goolbitg.api.model.SpendingTypeDto;
import com.goolbitg.api.model.UserAgreementDto;
import com.goolbitg.api.model.UserChecklistDto;
import com.goolbitg.api.model.UserDailyStatusDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.model.UserHabitDto;
import com.goolbitg.api.model.UserInfoDto;
import com.goolbitg.api.model.UserPatternDto;
import com.goolbitg.api.model.UserRegisterStatusDto;
import com.goolbitg.api.model.UserWeeklyStatusDto;
import com.goolbitg.api.repository.DailyRecordRepository;
import com.goolbitg.api.repository.RegistrationTokenRepository;
import com.goolbitg.api.repository.SpendingTypeRepository;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.util.FormatUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * UserService
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpendingTypeRepository spendingTypeRepository;
    @Autowired
    private DailyRecordRepository dailyRecordRepository;
    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;


    /* --------------- API Implements ----------------------*/

    @Override
    public UserDto getUser(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));

        return getUserDto(user);
    }

    @Override
    @Transactional
    public void updateUserInfo(String userId, UserInfoDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        if (user.getAgreement().getAgreement1() == null)
            throw UserException.previousStepNotComplete(0);

        if (isNicknameExistInner(request.getNickname())) {
            // TODO: Integrated validation error message required.
            throw new IllegalArgumentException(
                "이미 존재하는 닉네임입니다. (" + request.getNickname() + ")"
            );
        }

        user.updateInfo(
            request.getNickname(),
            request.getBirthday(),
            request.getGender()
        );
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateChecklistInfo(String userId, UserChecklistDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        if (user.getNickname() == null)
            throw UserException.previousStepNotComplete(1);

        UserSurvey survey = user.getSurvey();

        survey.updateChecklist(
            request.getCheck1(),
            request.getCheck2(),
            request.getCheck3(),
            request.getCheck4(),
            request.getCheck5(),
            request.getCheck6()
        );
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateHabitinfo(String userId, UserHabitDto request) {
        if (request.getAvgSavingPerMonth() > request.getAvgIncomePerMonth()) {
            throw UserException.savingIsGreaterThanIncome();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        if (user.getNickname() == null)
            throw UserException.previousStepNotComplete(1);

        UserSurvey survey = user.getSurvey();

        survey.updateHabit(
            request.getAvgIncomePerMonth(),
            request.getAvgSavingPerMonth()
        );
        SpendingType spendingType = spendingTypeRepository.findById(survey.getSpendingTypeId()).get();
        user.setSpendingType(spendingType);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePatternInfo(String userId, UserPatternDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        UserSurvey survey = user.getSurvey();

        survey.updatePattern(
            request.getPrimeUseDay(),
            FormatUtil.parseTime(request.getPrimeUseTime())
        );
        userRepository.save(user);
    }

    @Override
    public UserRegisterStatusDto getRegisterStatus(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> UserException.userNotExist(userId));

        UserRegisterStatusDto dto = new UserRegisterStatusDto();
        dto.setStatus(user.getRegisterStatus());
        dto.setRequiredInfoCompleted(user.isRequiredInfoCompleted());
        return dto;
    }

    @Override
    @Transactional
    public void updateAgreementInfo(String userId, UserAgreementDto request) {
        // TODO: throw error when required agreement is not agreed
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));

        user.updateAgreement(
            request.getAgreement1(),
            request.getAgreement2(),
            request.getAgreement3(),
            request.getAgreement4()
        );
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void postPushNotificationAgreement(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));

        user.getAgreement().allowPushNotification();
        userRepository.save(user);
    }

    @Override
    public UserWeeklyStatusDto getWeeklyStatus(String userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        UserStat stat = user.getStat();

        DayOfWeek todayOfWeek = date.getDayOfWeek();
        int todayIndex = todayOfWeek.getValue() - 1;
        LocalDate monday = date.minusDays(todayIndex);
        LocalDate sunday = date.plusDays(6 - todayIndex);
        List<DailyRecord> records = dailyRecordRepository.findByUserIdAndDateBetween(userId, monday, sunday);
        LocalDate dateOffset = monday;
        for (int i = 0; i < 7; i++) {
            if (records.size() < i + 1 || !records.get(i).getDate().equals(dateOffset)) {
                records.add(i, null);
            }
            dateOffset = dateOffset.plusDays(1);
        }

        return getWeeklyStatusDto(user, stat, todayIndex, records);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserStat(String userId, LocalDate date) {
        DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));

        UserStat stat = user.getStat();
        DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId)
                .orElseGet(() -> DailyRecord.getDefault(userId, date));

        if (dailyRecord.isCompleted()) {
            stat.increaseContinueCount();
        } else {
            stat.resetContinueCount();
        }

        userRepository.save(user);
    }

    @Override
    public NicknameCheckResponseDto isNicknameExist(NicknameCheckRequestDto nickname) {
        NicknameCheckResponseDto dto = new NicknameCheckResponseDto();
        dto.setDuplicated(isNicknameExistInner(nickname.getNickname()));
        return dto;
    }

    @Override
    @Transactional
    public void addRegistrationToken(String userId, RegistrationTokenRegisterDto registrationToken) {
        String tokenValue = registrationToken.getRegistrationToken();
        if (registrationTokenRepository.existsById(tokenValue)) {
            throw NoticeException.registrationTokenAlreadyRegistered(tokenValue);
        }

        RegistrationToken token = RegistrationToken.builder()
                .registrationToken(tokenValue)
                .userId(userId)
                .build();

        registrationTokenRepository.save(token);
    }


    /* ------------ DTO Mappers ------------- */

    private UserDto getUserDto(User user) {
        UserSurvey survey = user.getSurvey();
        UserStat stat = user.getStat();

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
        dto.setAvgSpendingPerMonth(survey.getAvgSavingPerMonth());
        dto.setSpendingHabitScore(survey.getSpendingHabitScore());
        dto.setPostCount(stat.getPostCount());
        dto.setChallengeCount(stat.getChallengeCount());
        dto.setAchievementGuage(stat.getAchievementGuage());

        if (user.getSpendingType() != null) {
            SpendingType spendingType = user.getSpendingType();
            Integer peopleCount = userRepository.countBySpendingTypeId(spendingType.getId());
            SpendingTypeDto spendingTypeDto = new SpendingTypeDto();
            spendingTypeDto.setId(spendingType.getId());
            spendingTypeDto.setTitle(spendingType.getTitle());
            spendingTypeDto.setImageUrl(URI.create(spendingType.getImageUrl()));
            spendingTypeDto.setProfileUrl(URI.create(spendingType.getProfileUrl()));
            spendingTypeDto.setOnboardingResultUrl(URI.create(spendingType.getOnboardingResultUrl()));
            spendingTypeDto.setPeopleCount(peopleCount);
            spendingTypeDto.setGoal(spendingType.getGoal());
            dto.setSpendingType(spendingTypeDto);
        }

        if (survey.getPrimeUseDay() != null)
            dto.setPrimeUseDay(survey.getPrimeUseDay().getValue());
        dto.setPrimeUseTime(FormatUtil.formatTime(survey.getPrimeUseTime()));

        return dto;
    }

    private UserWeeklyStatusDto getWeeklyStatusDto(User user, UserStat stat, int todayIndex, List<DailyRecord> records) {
        int saving = 0;
        UserWeeklyStatusDto dto = new UserWeeklyStatusDto();
        List<UserDailyStatusDto> weeklyStatus = new ArrayList<>();
        for (DailyRecord record : records) {
            UserDailyStatusDto dailyStatus = new UserDailyStatusDto();
            if (record == null) {
                dailyStatus.setTotalChallenges(0);
                dailyStatus.setAchievedChallenges(0);
            } else {
                dailyStatus.setTotalChallenges(record.getTotalChallenges());
                dailyStatus.setAchievedChallenges(record.getAchievedChallenges());
                saving += record.getSaving();
            }
            weeklyStatus.add(dailyStatus);
        }
        dto.setNickname(user.getNickname());
        dto.setContinueCount(stat.getContinueCount() + 1);
        dto.setSaving(saving);
        dto.setTodayIndex(todayIndex);
        dto.setWeeklyStatus(weeklyStatus);
        return dto;
    }


    /* ------------- Helper Methods ------------- */


    private boolean isNicknameExistInner(String nickname) {
        Optional<User> result = userRepository.findByNickname(nickname);
        return result.isPresent();
    }

}
