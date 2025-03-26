package com.goolbitg.api.v1.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.model.ChallengeGroupRecordDto;
import com.goolbitg.api.model.ChallengeGroupStatDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.PaginatedChallengeGroupDto;
import com.goolbitg.api.model.PaginatedChallengeGroupRecordDto;

/**
 * ChallengeGroupServiceImpl
 */
@Service
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    @Override
    public ChallengeGroupRecordDto checkChallengeGroup(String userId, Long groupId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkChallengeGroup'");
    }

    @Override
    public ChallengeGroupDto createChallengeGroup(String userId, ChallengeGroupDto challengeGroupDto) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createChallengeGroup'");
    }

    @Override
    public void deleteChallengeGroup(String userId, Long groupId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteChallengeGroup'");
    }

    @Override
    public void enrollChallengeGroup(String userId, Long groupId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enrollChallengeGroup'");
    }

    @Override
    public ChallengeGroupDto getChallengeGroup(String userId, Long groupId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChallengeGroup'");
    }

    @Override
    public ChallengeGroupRecordDto getChallengeGroupRecord(String userId, Long groupId, LocalDate date)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChallengeGroupRecord'");
    }

    @Override
    public PaginatedChallengeGroupRecordDto getChallengeGroupRecords(String userId, Integer page, Integer size,
            LocalDate date, ChallengeRecordStatus status, Boolean created) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChallengeGroupRecords'");
    }

    @Override
    public ChallengeGroupStatDto getChallengeGroupStat(String userId, Long groupId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChallengeGroupStat'");
    }

    @Override
    public PaginatedChallengeGroupDto getChallengeGroups(String userId, Integer page, Integer size, String search,
            Boolean created) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChallengeGroups'");
    }

    @Override
    public ChallengeGroupDto updateChallengeGroup(String userId, Long groupId, ChallengeGroupDto challengeGroupDto)
            throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateChallengeGroup'");
    }

}
