package com.goolbitg.api.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.Challenge;
import com.goolbitg.api.entity.ChallengeRecord;
import com.goolbitg.api.entity.ChallengeRecordId;
import com.goolbitg.api.entity.ChallengeStat;
import com.goolbitg.api.entity.ChallengeStatId;
import com.goolbitg.api.entity.DailyRecord;
import com.goolbitg.api.entity.DailyRecordId;
import com.goolbitg.api.entity.SpendingType;
import com.goolbitg.api.entity.User;
import com.goolbitg.api.entity.UserStat;
import com.goolbitg.api.exception.ChallengeException;
import com.goolbitg.api.exception.UserException;
import com.goolbitg.api.model.ChallengeDto;
import com.goolbitg.api.model.ChallengeRecordDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.ChallengeStatDto;
import com.goolbitg.api.model.ChallengeTrippleDto;
import com.goolbitg.api.model.PaginatedChallengeDto;
import com.goolbitg.api.model.PaginatedChallengeRecordDto;
import com.goolbitg.api.repository.ChallengeRecordRepository;
import com.goolbitg.api.repository.ChallengeRepository;
import com.goolbitg.api.repository.ChallengeStatRepository;
import com.goolbitg.api.repository.DailyRecordRepository;
import com.goolbitg.api.repository.SpendingTypeRepository;
import com.goolbitg.api.repository.UserRepository;
import com.goolbitg.api.repository.UserStatRepository;

import lombok.RequiredArgsConstructor;

/**
 * ChallengeServiceImpl
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    private final ChallengeRepository challengeRepository;
    @Autowired
    private final ChallengeRecordRepository challengeRecordRepository;
    @Autowired
    private final ChallengeStatRepository challengeStatRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserStatRepository userStatRepository;
    @Autowired
    private final DailyRecordRepository dailyRecordRepository;
    @Autowired
    private final SpendingTypeRepository spendingTypeRepository;



    /*  ----------- API Implementations ----------- */

    @Override
    public ChallengeDto getChallenge(Long challengeId) {
        Optional<Challenge> result = challengeRepository.findById(challengeId);
        if (result.isEmpty()) {
            throw ChallengeException.challengeNotExist(challengeId);
        }

        Challenge challenge = result.get();
        return getChallengeDto(challenge);
    }

    @Override
    public PaginatedChallengeDto getChallenges(Integer page, Integer size, Long spendingTypeId) {
        Page<Challenge> result;
        Pageable pageReq = PageRequest.of(page, size);
        if (spendingTypeId == null) {
            result = challengeRepository.findAll(pageReq);
        } else {
            result = challengeRepository.findAllBySpendingTypeId(spendingTypeId, pageReq);
        }

        return getPaginatedChallengeDto(result);
    }

    @Override
    @Transactional
    public void cancelChallenge(String userId, Long challengeId, LocalDate date) {
        ChallengeRecordId challengeRecordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeRecord record = challengeRecordRepository.findById(challengeRecordId)
                .orElseThrow(() -> ChallengeException.notEnrolled(challengeId));
        ChallengeStatId challengeStatId = new ChallengeStatId(challengeId, userId);
        ChallengeStat challengeStat = challengeStatRepository.findById(challengeStatId).get();

        DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
        DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId).get();

        if (record.getStatus() == ChallengeRecordStatus.WAIT) {
            challengeRecordRepository.delete(record);
            dailyRecord.setTotalChallenges(dailyRecord.getTotalChallenges() - 1);
        }
        for (int i = 1; i < 3; i++) {
            date = date.plusDays(1);
            challengeRecordId = new ChallengeRecordId(challengeId, userId, date);
            challengeRecordRepository.deleteById(challengeRecordId);
        }
        challengeStat.setEnrollCount(challengeStat.getEnrollCount() - 1);

        challengeStatRepository.save(challengeStat);
        dailyRecordRepository.save(dailyRecord);
    }

    @Override
    @Transactional
    public ChallengeRecordDto checkChallenge(String userId, Long challengeId, LocalDate date) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, date);
        Optional<ChallengeRecord> recordResult = challengeRecordRepository.findById(recordId);
        ChallengeStatId statId = new ChallengeStatId(challengeId, userId);
        Optional<ChallengeStat> statResult = challengeStatRepository.findById(statId);

        if (recordResult.isEmpty() || statResult.isEmpty()) {
            throw ChallengeException.notEnrolled(challengeId);
        }
        ChallengeRecord record = recordResult.get();
        ChallengeStat challengeStat = statResult.get();
        if (record.getStatus() != ChallengeRecordStatus.WAIT) {
            throw ChallengeException.alreadyComplete(challengeId);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        UserStat userStat = userStatRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
        DailyRecord dailyRecord = findOrCreateDailyRecord(userId, date, dailyRecordId);

        record.setStatus(ChallengeRecordStatus.SUCCESS);
        challengeStat.setTotalCount(challengeStat.getTotalCount() + 1);
        challengeStat.setCurrentContinueCount(challengeStat.getCurrentContinueCount() + 1);
        if (challengeStat.getCurrentContinueCount() > challengeStat.getContinueCount()) {
            challengeStat.setContinueCount(challengeStat.getCurrentContinueCount());
        }
        challengeRecordRepository.save(record);
        challengeStatRepository.save(challengeStat);

        increaseAchievementGuage(challenge, user, userStat);
        dailyRecord.setSaving(dailyRecord.getSaving() + challenge.getReward());
        dailyRecord.setAchievedChallenges(dailyRecord.getAchievedChallenges() + 1);

        userRepository.save(user);
        userStatRepository.save(userStat);
        dailyRecordRepository.save(dailyRecord);

        return getChallengeRecordDto(challenge, record);
    }

    private void increaseAchievementGuage(Challenge challenge, User user, UserStat userStat) {
        SpendingType currentType = spendingTypeRepository.findById(user.getSpendingTypeId()).get();
        int newGuage = userStat.getAchievementGuage() + challenge.getReward();

        if (currentType.getGoal() != null && newGuage >= currentType.getGoal()) {
            SpendingType newType = spendingTypeRepository.findById(user.getSpendingTypeId() + 1).orElseGet(null);
            newGuage = newGuage - currentType.getGoal();
            user.setSpendingTypeId(newType.getId());
        }
        userStat.setAchievementGuage(newGuage);
    }

    @Override
    @Transactional
    public void enrollChallenge(String userId, Long challengeId, LocalDate date) {
        UserStat userStat = userStatRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        DailyRecordId dailyRecordId = new DailyRecordId(userId, date);
        DailyRecord dailyRecord = findOrCreateDailyRecord(userId, date, dailyRecordId);
        ChallengeStatId id = new ChallengeStatId(challengeId, userId);
        ChallengeStat challengeStat = challengeStatRepository.findById(id)
                .orElse(new ChallengeStat());

        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, date);
        Optional<ChallengeRecord> recordResult = challengeRecordRepository.findById(recordId);
        int startDay = 0;
        if (recordResult.isPresent()) {
            if (recordResult.get().getStatus() == ChallengeRecordStatus.SUCCESS) {
                startDay = 1;
            } else {
                throw ChallengeException.alreadyEnrolled(challengeId);
            }
        }

        challengeStat.setUserId(userId);
        challengeStat.setChallengeId(challengeId);
        Integer prevEnrollCount = challengeStat.getEnrollCount();
        if (prevEnrollCount == null) {
            // New Enrollment
            prevEnrollCount = 0;
            userStat.setChallengeCount(userStat.getChallengeCount() + 1);
        }
        challengeStat.setEnrollCount(prevEnrollCount + 1);
        if (challengeStat.getTotalCount() == null) challengeStat.setTotalCount(0);
        if (challengeStat.getContinueCount() == null) challengeStat.setContinueCount(0);

        challengeStatRepository.save(challengeStat);

        for (int i = 0; i < 3; i++) {
            ChallengeRecord record = new ChallengeRecord();
            record.setChallengeId(challengeId);
            record.setUserId(userId);
            record.setDate(date.plusDays(i + startDay));
            record.setStatus(ChallengeRecordStatus.WAIT);
            record.setLocation(i + 1);
            challengeRecordRepository.save(record);
        }

        userStatRepository.save(userStat);

        dailyRecord.setTotalChallenges(dailyRecord.getTotalChallenges() + 1);
        dailyRecordRepository.save(dailyRecord);
    }

    private DailyRecord findOrCreateDailyRecord(String userId, LocalDate date, DailyRecordId dailyRecordId) {
        DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId)
                .orElseGet(() -> {
            DailyRecord entity = new DailyRecord();
            entity.setUserId(userId);
            entity.setDate(date);
            entity.setSaving(0);
            entity.setTotalChallenges(0);
            entity.setAchievedChallenges(0);
            return entity;
        });
        return dailyRecord;
    }

    @Override
    public ChallengeRecordDto getChallengeRecord(String userId, Long challengeId, LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Date should not be null");

        ChallengeRecordId id = new ChallengeRecordId(challengeId, userId, date);
        Optional<ChallengeRecord> result = challengeRecordRepository.findById(id);

        if (result.isEmpty()) {
            throw ChallengeException.challengeRecordNotExist(challengeId);
        }
        ChallengeRecord record = result.get();

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));

        return getChallengeRecordDto(challenge, record);
    }

    @Override
    public PaginatedChallengeRecordDto getChallengeRecords(String userId, Integer page, Integer size, LocalDate date, ChallengeRecordStatus status) {
        PageRequest pageReq = PageRequest.of(page, size);
        if (date == null)
            throw new IllegalArgumentException("Date should not be null");

        Page<ChallengeRecord> result;
        if (status == null) {
            result = challengeRecordRepository.findAllByUserIdAndDate(pageReq, userId, date);
        } else {
            result = challengeRecordRepository.findAllByUserIdAndDateAndStatus(pageReq, userId, date, status);
        }

        return getPaginatedRecordDto(result);
    }

    @Override
    public ChallengeStatDto getChallengeStat(String userId, Long challengeId) {
        ChallengeStatId id = new ChallengeStatId(challengeId, userId);
        ChallengeStat stat = challengeStatRepository.findById(id)
                .orElseThrow(() -> ChallengeException.challengeRecordNotExist(challengeId));

        return getChallengeStatDto(stat);
    }

    @Override
    public ChallengeTrippleDto getChallengeTripple(String userId, Long challengeId, LocalDate date) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeRecord currentRecord = challengeRecordRepository.findById(recordId)
                .orElseThrow(() -> ChallengeException.notEnrolled(challengeId));
        ChallengeStatId statId = new ChallengeStatId(challengeId, userId);
        ChallengeStat stat = challengeStatRepository.findById(statId).get();

        List<ChallengeRecord> records = new ArrayList<>();
        date = date.minusDays(currentRecord.getLocation() - 1);
        for (int i = 0; i < 3; i++) {
            recordId = new ChallengeRecordId(challengeId, userId, date);
            records.add(challengeRecordRepository.findById(recordId).get());
            date = date.plusDays(1);
        }
        return getChallengeTrippleDto(challenge, records, currentRecord, stat);
    }



    /*  --------------- DTO Mappers ------------- */

    private ChallengeDto getChallengeDto(Challenge challenge) {
        ChallengeDto dto = new ChallengeDto();
        dto.setId(challenge.getId());
        dto.setTitle(challenge.getTitle());
        dto.setImageUrl(URI.create(challenge.getImageUrl()));
        dto.setReward(challenge.getReward());
        dto.setMaxAchieveDays(challenge.getMaxAchieveDays());
        dto.setAvgAchieveRatio(challenge.getAvgAchieveRatio());
        dto.setParticipantCount(challenge.getParticipantCount());
        return dto;
    }

    private PaginatedChallengeDto getPaginatedChallengeDto(Page<Challenge> result) {
        PaginatedChallengeDto dto = new PaginatedChallengeDto();
        dto.setTotalPages(result.getTotalPages());
        dto.setTotalSize((int)result.getTotalElements());
        dto.setPage(result.getNumber());
        dto.setSize((int)result.get().count());
        dto.setItems(result.stream().map(e -> {
            return getChallengeDto(e);
        }).toList());

        return dto;
    }

    private ChallengeRecordDto getChallengeRecordDto(Challenge challenge, ChallengeRecord record) {
        ChallengeRecordDto dto = new ChallengeRecordDto();
        dto.setChallenge(getChallengeDto(challenge));
        dto.setUserId(record.getUserId());
        dto.setStatus(record.getStatus());
        dto.setDate(record.getDate());
        dto.setLocation(record.getLocation());
        return dto;
    }

    private PaginatedChallengeRecordDto getPaginatedRecordDto(Page<ChallengeRecord> result) {
        PaginatedChallengeRecordDto dto = new PaginatedChallengeRecordDto();
        dto.setTotalPages(result.getTotalPages());
        dto.setTotalSize((int)result.getTotalElements());
        dto.setPage(result.getNumber());
        dto.setSize((int)result.get().count());
        dto.setItems(result.map(e -> {
            Challenge c = challengeRepository.findById(e.getChallengeId()).get();
            return getChallengeRecordDto(c, e);
        }).toList());

        return dto;
    }

    private ChallengeStatDto getChallengeStatDto(ChallengeStat stat) {
        ChallengeStatDto dto = new ChallengeStatDto();
        dto.setUserId(stat.getUserId());
        dto.setChallengeId(stat.getChallengeId());
        dto.setTotalCount(stat.getTotalCount());
        dto.setEnrollCount(stat.getEnrollCount());
        dto.setContinueCount(stat.getContinueCount());
        return dto;
    }

    private ChallengeTrippleDto getChallengeTrippleDto(Challenge challenge, List<ChallengeRecord> records, ChallengeRecord currentRecord, ChallengeStat stat) {
        ChallengeTrippleDto dto = new ChallengeTrippleDto();
        dto.setChallenge(getChallengeDto(challenge));
        if (currentRecord.getStatus() == ChallengeRecordStatus.WAIT)
            dto.setDuration(stat.getContinueCount() + 1);
        else
            dto.setDuration(stat.getContinueCount());
        dto.setCheck1(records.get(0).getStatus());
        dto.setCheck2(records.get(1).getStatus());
        dto.setCheck3(records.get(2).getStatus());
        dto.setLocation(currentRecord.getLocation());
        return dto;
    }

}
