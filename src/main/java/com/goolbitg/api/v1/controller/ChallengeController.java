package com.goolbitg.api.v1.controller;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.ChallengeApi;
import com.goolbitg.api.model.ChallengeDto;
import com.goolbitg.api.model.ChallengeRecordDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.ChallengeStatDto;
import com.goolbitg.api.model.ChallengeTrippleDto;
import com.goolbitg.api.model.PaginatedChallengeDto;
import com.goolbitg.api.model.PaginatedChallengeRecordDto;
import com.goolbitg.api.v1.security.AuthUtil;
import com.goolbitg.api.v1.service.ChallengeService;
import com.goolbitg.api.v1.service.TimeService;

import lombok.RequiredArgsConstructor;

/**
 * ChallengeController
 */
@RestController
@RequiredArgsConstructor
public class ChallengeController implements ChallengeApi {

    @Autowired
    private final ChallengeService challengeService;
    @Autowired
    private final TimeService timeService;

    @Override
    public ResponseEntity<ChallengeTrippleDto> getChallengeTripple(Long challengeId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        LocalDate date = timeService.getToday();

        ChallengeTrippleDto result = challengeService.getChallengeTripple(userId, challengeId, date);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeDto> getChallenge(Long challengeId) throws Exception {
        ChallengeDto result = challengeService.getChallenge(challengeId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> cancelChallenge(Long challengeId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        LocalDate date = timeService.getToday();

        challengeService.cancelChallenge(userId, challengeId, date);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ChallengeRecordDto> checkChallenge(Long challengeId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        LocalDate date = timeService.getToday();

        ChallengeRecordDto result = challengeService.checkChallenge(userId, challengeId, date);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> enrollChallenge(Long challengeId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        LocalDate date = timeService.getToday();

        challengeService.enrollChallenge(userId, challengeId, date);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ChallengeRecordDto> getChallengeRecord(Long challengeId, @Valid LocalDate date)
            throws Exception {
        String userId = AuthUtil.getLoginUserId();
        if (date == null) date = timeService.getToday();

        ChallengeRecordDto result = challengeService.getChallengeRecord(userId, challengeId, date);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaginatedChallengeRecordDto> getChallengeRecords(@Valid Integer page, @Valid Integer size,
            @Valid LocalDate date, @Valid ChallengeRecordStatus status) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        if (date == null) date = timeService.getToday();

        PaginatedChallengeRecordDto result = challengeService.getChallengeRecords(userId, page, size, date, status);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeStatDto> getChallengeStat(Long challengeId) throws Exception {
        String userId = AuthUtil.getLoginUserId();

        ChallengeStatDto result = challengeService.getChallengeStat(userId, challengeId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaginatedChallengeDto> getChallenges(@Valid Integer page, @Valid Integer size) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        PaginatedChallengeDto result = challengeService.getChallenges(page, size, userId);
        return ResponseEntity.ok(result);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
