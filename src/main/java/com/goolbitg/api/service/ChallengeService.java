package com.goolbitg.api.service;

import java.time.LocalDate;

import com.goolbitg.api.model.ChallengeDto;
import com.goolbitg.api.model.ChallengeRecordDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.ChallengeStatDto;
import com.goolbitg.api.model.ChallengeTrippleDto;
import com.goolbitg.api.model.PaginatedChallengeDto;
import com.goolbitg.api.model.PaginatedChallengeRecordDto;

/**
 * ChallengeService
 */
public interface ChallengeService {

    ChallengeDto getChallenge(Long challengeId);
    PaginatedChallengeDto getChallenges(Integer page, Integer size, String userId);
    void cancelChallenge(String userId, Long challengeId, LocalDate date);
    ChallengeRecordDto checkChallenge(String userId, Long challengeId, LocalDate date);
    void enrollChallenge(String userId, Long challengeId, LocalDate date);
    ChallengeRecordDto getChallengeRecord(String userId, Long challengeId, LocalDate date);
    PaginatedChallengeRecordDto getChallengeRecords(String userId, Integer page, Integer size, LocalDate date, ChallengeRecordStatus status);
    ChallengeStatDto getChallengeStat(String userId, Long challengeId);
    ChallengeTrippleDto getChallengeTripple(String userId, Long challengeId, LocalDate date);
    void calculateChallengeStat(Long challengeId, LocalDate date);
    void calculateAllChallengeStat(LocalDate date);

}
