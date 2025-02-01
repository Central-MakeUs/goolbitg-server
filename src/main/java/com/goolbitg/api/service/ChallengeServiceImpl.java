package com.goolbitg.api.service;

import java.net.URI;
import java.time.Clock;
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
    private final UserStatRepository userStatRepository;
    @Autowired
    private final DailyRecordRepository dailyRecordRepository;
    @Autowired
    private final Clock clock;



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
    public void cancelChallenge(String userId, Long challengeId) {
        LocalDate date = getToday();
        ChallengeRecordId id = new ChallengeRecordId(challengeId, userId, date);
        Optional<ChallengeRecord> result = challengeRecordRepository.findById(id);

        if (result.isEmpty()) {
            throw ChallengeException.notEnrolled(challengeId);
        }
        ChallengeRecord record = result.get();
        if (record.getStatus() == ChallengeRecordStatus.WAIT) {
            challengeRecordRepository.delete(record);
        }
        for (int i = 1; i < 3; i++) {
            date = date.plusDays(1);
            id = new ChallengeRecordId(challengeId, userId, date);
            challengeRecordRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public ChallengeRecordDto checkChallenge(String userId, Long challengeId) {
        LocalDate date = getToday();
        ChallengeRecordId id = new ChallengeRecordId(challengeId, userId, date);
        Optional<ChallengeRecord> result = challengeRecordRepository.findById(id);
        ChallengeStatId statId = new ChallengeStatId(challengeId, userId);
        Optional<ChallengeStat> statResult = challengeStatRepository.findById(statId);

        if (result.isEmpty() || statResult.isEmpty()) {
            throw ChallengeException.notEnrolled(challengeId);
        }
        ChallengeRecord record = result.get();
        ChallengeStat stat = statResult.get();
        if (record.getStatus() != ChallengeRecordStatus.WAIT) {
            throw ChallengeException.alreadyComplete(challengeId);
        }

        record.setStatus(ChallengeRecordStatus.SUCCESS);
        stat.setTotalCount(stat.getTotalCount() + 1);
        stat.setCurrentContinueCount(stat.getCurrentContinueCount() + 1);
        if (stat.getCurrentContinueCount() > stat.getContinueCount()) {
            stat.setContinueCount(stat.getCurrentContinueCount());
        }
        challengeRecordRepository.save(record);
        challengeStatRepository.save(stat);

        return getChallengeRecordDto(record);
    }

    @Override
    @Transactional
    public void enrollChallenge(String userId, Long challengeId) {
        UserStat userStat = userStatRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        DailyRecordId dailyRecordId = new DailyRecordId(userId, getToday());
        DailyRecord dailyRecord = dailyRecordRepository.findById(dailyRecordId)
                .orElseGet(() -> {
            DailyRecord entity = new DailyRecord();
            entity.setUserId(userId);
            entity.setDate(getToday());
            entity.setSaving(0);
            entity.setTotalChallenges(0);
            entity.setAchievedChallenges(0);
            return entity;
        });
        ChallengeStatId id = new ChallengeStatId(challengeId, userId);
        ChallengeStat challengeStat = challengeStatRepository.findById(id)
                .orElse(new ChallengeStat());

        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, getToday());
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
            LocalDate date = getToday();
            date = date.plusDays(i + startDay);
            ChallengeRecord record = new ChallengeRecord();
            record.setChallengeId(challengeId);
            record.setUserId(userId);
            record.setDate(date);
            record.setStatus(ChallengeRecordStatus.WAIT);
            record.setLocation(i + 1);
            challengeRecordRepository.save(record);
        }

        userStatRepository.save(userStat);

        dailyRecord.setTotalChallenges(dailyRecord.getTotalChallenges() + 1);
        dailyRecordRepository.save(dailyRecord);
    }

    @Override
    public ChallengeRecordDto getChallengeRecord(String userId, Long challengeId, LocalDate date) {
        if (date == null) {
            date = getToday();
        }
        ChallengeRecordId id = new ChallengeRecordId(challengeId, userId, date);
        Optional<ChallengeRecord> result = challengeRecordRepository.findById(id);

        if (result.isEmpty()) {
            throw ChallengeException.challengeRecordNotExist(challengeId);
        }
        ChallengeRecord record = result.get();

        return getChallengeRecordDto(record);
    }

    @Override
    public PaginatedChallengeRecordDto getChallengeRecords(String userId, Integer page, Integer size, LocalDate date, ChallengeRecordStatus status) {
        PageRequest pageReq = PageRequest.of(page, size);
        if (date == null) {
            date = getToday();
        }

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
    public ChallengeTrippleDto getChallengeTripple(String userId, Long challengeId) {
        LocalDate date = getToday();
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
        return getChallengeTrippleDto(challengeId, records, currentRecord, stat);
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

    private ChallengeRecordDto getChallengeRecordDto(ChallengeRecord record) {
        ChallengeRecordDto dto = new ChallengeRecordDto();
        dto.setChallengeId(record.getChallengeId());
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
            return getChallengeRecordDto(e);
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

    private ChallengeTrippleDto getChallengeTrippleDto(Long challengeId, List<ChallengeRecord> records, ChallengeRecord currentRecord, ChallengeStat stat) {
        ChallengeTrippleDto dto = new ChallengeTrippleDto();
        dto.setChallengeId(challengeId);
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



    /* ----------- Helper Methods -------------- */

    private LocalDate getToday() {
        return LocalDate.now(clock);
    }

}
