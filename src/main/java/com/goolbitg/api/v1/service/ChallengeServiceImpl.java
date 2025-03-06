package com.goolbitg.api.v1.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.model.ChallengeDto;
import com.goolbitg.api.model.ChallengeRecordDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.ChallengeStatDto;
import com.goolbitg.api.model.ChallengeTrippleDto;
import com.goolbitg.api.model.PaginatedChallengeDto;
import com.goolbitg.api.model.PaginatedChallengeRecordDto;
import com.goolbitg.api.v1.entity.Challenge;
import com.goolbitg.api.v1.entity.ChallengeRecord;
import com.goolbitg.api.v1.entity.ChallengeRecordId;
import com.goolbitg.api.v1.entity.ChallengeStat;
import com.goolbitg.api.v1.entity.ChallengeStatId;
import com.goolbitg.api.v1.entity.User;
import com.goolbitg.api.v1.event.challenge.ChallengeCancelEvent;
import com.goolbitg.api.v1.event.challenge.ChallengeCheckEvent;
import com.goolbitg.api.v1.event.challenge.ChallengeEnrollEvent;
import com.goolbitg.api.v1.exception.ChallengeException;
import com.goolbitg.api.v1.exception.UserException;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeRepository;
import com.goolbitg.api.v1.repository.ChallengeStatRepository;
import com.goolbitg.api.v1.repository.UserRepository;

/**
 * ChallengeServiceImpl
 */
@Service
@Transactional(readOnly = true)
public class ChallengeServiceImpl implements ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ChallengeRecordRepository challengeRecordRepository;
    @Autowired
    private ChallengeStatRepository challengeStatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;



    /*  ----------- API Implementations ----------- */

    @Override
    public ChallengeDto getChallenge(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));

        return getChallengeDto(challenge);
    }

    @Override
    public PaginatedChallengeDto getChallenges(Integer page, Integer size, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserException.userNotExist(userId));
        Pageable pageReq = PageRequest.of(page, size);

        Page<Challenge> result = challengeRepository.findAllBySpendingTypeId(user.getSpendingType(), pageReq);

        return getPaginatedChallengeDto(result);
    }

    @Override
    @Transactional
    public void cancelChallenge(String userId, Long challengeId, LocalDate date) {
        ChallengeRecordId challengeRecordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeRecord todayRecord = challengeRecordRepository.findById(challengeRecordId)
                .orElseThrow(() -> ChallengeException.notEnrolled(challengeId));
        boolean checkedToday = todayRecord.getStatus().equals(ChallengeRecordStatus.SUCCESS);

        if (!checkedToday) {
            todayRecord.fail();
        }
        for (int i = todayRecord.getLocation() + 1; i <= 3; i++) {
            challengeRecordId = challengeRecordId.next();
            ChallengeRecord challengeRecord = challengeRecordRepository.findById(challengeRecordId)
                    .orElseThrow(() -> ChallengeException.challengeRecordNotExist(challengeId));
            challengeRecord.fail();
        }

        applicationEventPublisher.publishEvent(
            new ChallengeCancelEvent(this, userId, challengeId, date, checkedToday)
        );
    }

    @Override
    @Transactional
    public ChallengeRecordDto checkChallenge(String userId, Long challengeId, LocalDate date) {
        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeStatId statId = new ChallengeStatId(challengeId, userId);
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        ChallengeRecord record = challengeRecordRepository.findById(recordId)
                .orElseThrow(() -> ChallengeException.notEnrolled(challengeId));
        ChallengeStat stat = challengeStatRepository.findById(statId)
                .orElseThrow();

        if (record.getStatus().equals(ChallengeRecordStatus.FAIL)) {
            throw ChallengeException.notEnrolled(challengeId);
        }
        if (record.getStatus().equals(ChallengeRecordStatus.SUCCESS)) {
            throw ChallengeException.alreadyComplete(challengeId);
        }

        record.success();
        challenge.achieve();

        applicationEventPublisher.publishEvent(
            new ChallengeCheckEvent(this, userId, challengeId, date, challenge.getReward())
        );
        return getChallengeRecordDto(challenge, record, stat);
    }

    @Override
    @Transactional
    public void enrollChallenge(String userId, Long challengeId, LocalDate date) {
        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, date);
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        Optional<ChallengeRecord> recordResult = challengeRecordRepository.findById(recordId);

        int startDay = 0;
        if (recordResult.isPresent()) {
            if (recordResult.get().getStatus() == ChallengeRecordStatus.SUCCESS &&
                recordResult.get().getLocation() == 3) {
                startDay = 1;
            } else if (!recordResult.get().getStatus().equals(ChallengeRecordStatus.FAIL)) {
                throw ChallengeException.alreadyEnrolled(challengeId);
            }
        }

        for (int i = 0; i < 3; i++) {
            ChallengeRecord record = ChallengeRecord.builder()
                    .challengeId(challengeId)
                    .userId(userId)
                    .date(date.plusDays(i + startDay))
                    .status(ChallengeRecordStatus.WAIT)
                    .location(i + 1)
                    .build();
            challengeRecordRepository.save(record);
        }

        challenge.enroll();

        applicationEventPublisher.publishEvent(
            new ChallengeEnrollEvent(this, userId, challengeId, date, startDay == 1)
        );
    }

    @Override
    public ChallengeRecordDto getChallengeRecord(String userId, Long challengeId, LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Date should not be null");

        ChallengeRecordId challengeRecordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeStatId statId = new ChallengeStatId(challengeId, userId);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        ChallengeRecord record = challengeRecordRepository.findById(challengeRecordId)
                .orElseThrow(() -> ChallengeException.challengeRecordNotExist(challengeId));
        ChallengeStat stat = challengeStatRepository.findById(statId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));

        return getChallengeRecordDto(challenge, record, stat);
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

        return getPaginatedRecordDto(result, userId);
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
        ChallengeRecordId recordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeStatId statId = new ChallengeStatId(challengeId, userId);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        ChallengeRecord currentRecord = challengeRecordRepository.findById(recordId)
                .orElseThrow(() -> ChallengeException.notEnrolled(challengeId));
        if (currentRecord.getStatus().equals(ChallengeRecordStatus.FAIL)) {
            throw ChallengeException.notEnrolled(challengeId);
        }

        ChallengeStat stat = challengeStatRepository.findById(statId).get();

        List<ChallengeRecord> records = new ArrayList<>();
        boolean canceled = false;
        date = date.minusDays(currentRecord.getLocation() - 1);
        for (int i = 0; i < 3; i++) {
            recordId = new ChallengeRecordId(challengeId, userId, date);
            Optional<ChallengeRecord> result = challengeRecordRepository.findById(recordId);
            ChallengeRecord record;
            if (result.isEmpty() || result.get().getStatus().equals(ChallengeRecordStatus.FAIL)) {
                record = ChallengeRecord.getEmpty();
                canceled = true;
            } else {
                record = result.get();
            }
            records.add(record);
            date = date.plusDays(1);
        }
        return getChallengeTrippleDto(challenge, records, currentRecord, stat, canceled);
    }

    @Override
    @Transactional
    public void calculateChallengeStat(Long challengeId, LocalDate date) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        int participantCount = challengeRecordRepository.countByChallengeIdAndDate(challengeId, date);
        int maxAchieveDays = challengeStatRepository.getMaxContinueCountByChallengeId(challengeId);

        challenge.updateStats(participantCount, maxAchieveDays);
    }

    @Override
    @Transactional
    public void calculateAllChallengeStat(LocalDate date) {
        for (Long challengeId : challengeRepository.findAllIds()) {
            calculateChallengeStat(challengeId, date);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failChallenge(String userId, Long challengeId, LocalDate date) {
        ChallengeRecordId challengeRecordId = new ChallengeRecordId(challengeId, userId, date);
        ChallengeStatId challengeStatId = new ChallengeStatId(challengeId, userId);
        ChallengeRecord todayRecord = challengeRecordRepository.findById(challengeRecordId)
                .orElseThrow(() -> ChallengeException.challengeRecordNotExist(challengeId));
        ChallengeStat challengeStat = challengeStatRepository.findById(challengeStatId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(challengeId));
        if (todayRecord.getStatus().equals(ChallengeRecordStatus.SUCCESS)) {
            throw ChallengeException.alreadyComplete(challengeId);
        }
        todayRecord.fail();
        for (int i = todayRecord.getLocation() + 1; i <= 3; i++) {
            challengeRecordId = challengeRecordId.next();
            ChallengeRecord challengeRecord = challengeRecordRepository.findById(challengeRecordId)
                    .orElseThrow(() -> ChallengeException.challengeRecordNotExist(challengeId));
            challengeRecord.fail();
        }
        challengeStat.fail();
    }


    /*  --------------- DTO Mappers ------------- */

    private ChallengeDto getChallengeDto(Challenge challenge) {
        ChallengeDto dto = new ChallengeDto();
        dto.setId(challenge.getId());
        dto.setTitle(challenge.getTitle());
        dto.setImageUrlLarge(URI.create(challenge.getImageUrlLarge()));
        dto.setImageUrlSmall(URI.create(challenge.getImageUrlSmall()));
        dto.setReward(challenge.getReward());
        dto.setMaxAchieveDays(challenge.getMaxAchieveDays());
        dto.setAvgAchieveRatio(Math.round(challenge.getAvgAchieveRatio()));
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

    private ChallengeRecordDto getChallengeRecordDto(Challenge challenge, ChallengeRecord record, ChallengeStat stat) {
        ChallengeRecordDto dto = new ChallengeRecordDto();
        dto.setChallenge(getChallengeDto(challenge));
        dto.setUserId(record.getUserId());
        dto.setStatus(record.getStatus());
        dto.setDate(record.getDate());
        dto.setLocation(record.getLocation());
        dto.setDuration(getChallengeDuration(stat, record));
        return dto;
    }

    private PaginatedChallengeRecordDto getPaginatedRecordDto(Page<ChallengeRecord> result, String userId) {
        PaginatedChallengeRecordDto dto = new PaginatedChallengeRecordDto();
        dto.setTotalPages(result.getTotalPages());
        dto.setTotalSize((int)result.getTotalElements());
        dto.setPage(result.getNumber());
        dto.setSize((int)result.get().count());
        dto.setItems(result.map(e -> {
            Challenge c = challengeRepository.findById(e.getChallengeId()).get();
            ChallengeStatId statId = new ChallengeStatId(e.getChallengeId(), userId);
            ChallengeStat s = challengeStatRepository.findById(statId).get();
            return getChallengeRecordDto(c, e, s);
        }).toList());
        int totalReward = 0;
        for (ChallengeRecordDto cr : dto.getItems()) {
            totalReward += cr.getChallenge().getReward();
        }
        dto.setTotalReward(totalReward);

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

    private ChallengeTrippleDto getChallengeTrippleDto(Challenge challenge, List<ChallengeRecord> records, ChallengeRecord currentRecord, ChallengeStat stat, boolean canceled) {
        ChallengeTrippleDto dto = new ChallengeTrippleDto();
        dto.setChallenge(getChallengeDto(challenge));
        dto.setDuration(getChallengeDuration(stat, currentRecord));
        dto.setCheck1(records.get(0).getStatus());
        dto.setCheck2(records.get(1).getStatus());
        dto.setCheck3(records.get(2).getStatus());
        dto.setLocation(currentRecord.getLocation());
        dto.setCanceled(canceled);
        return dto;
    }

    private int getChallengeDuration(ChallengeStat stat, ChallengeRecord record) {
        if (record.getStatus() == ChallengeRecordStatus.WAIT)
            return stat.getContinueCount() + 1;
        else
            return stat.getContinueCount();

    }
}
