package com.goolbitg.api.v1.service;

import java.time.LocalDate;

import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.model.ChallengeGroupRankDto;
import com.goolbitg.api.model.ChallengeGroupRecordDto;
import com.goolbitg.api.model.ChallengeGroupStatDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.PaginatedChallengeGroupDto;
import com.goolbitg.api.model.PaginatedChallengeGroupRecordDto;

/**
 * ChallengeGroupService
 */
public interface ChallengeGroupService {

    ChallengeGroupRecordDto checkChallengeGroup(String userId, Long groupId) throws Exception;
    ChallengeGroupDto createChallengeGroup(String userId, ChallengeGroupDto challengeGroupDto) throws Exception;
    void deleteChallengeGroup(String userId, Long groupId) throws Exception;
    void enrollChallengeGroup(String userId, Long groupId) throws Exception;
    ChallengeGroupRankDto getChallengeGroup(Long groupId) throws Exception;
    ChallengeGroupRecordDto getChallengeGroupRecord(String userId, Long groupId, LocalDate date) throws Exception;
    PaginatedChallengeGroupRecordDto getChallengeGroupRecords(String userId, int page, int size, LocalDate date, ChallengeRecordStatus status, Boolean created) throws Exception;
    ChallengeGroupStatDto getChallengeGroupStat(String userId, Long groupId) throws Exception;
    PaginatedChallengeGroupDto getChallengeGroups(String userId, int page, int size, String search, Boolean created) throws Exception;
    ChallengeGroupDto updateChallengeGroup(String userId, Long groupId, ChallengeGroupDto challengeGroupDto) throws Exception;

}
