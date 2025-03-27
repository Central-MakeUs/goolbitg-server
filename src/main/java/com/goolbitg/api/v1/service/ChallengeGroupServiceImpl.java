package com.goolbitg.api.v1.service;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.model.ChallengeGroupRecordDto;
import com.goolbitg.api.model.ChallengeGroupStatDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.PaginatedChallengeGroupDto;
import com.goolbitg.api.model.PaginatedChallengeGroupRecordDto;
import com.goolbitg.api.v1.entity.ChallengeGroup;
import com.goolbitg.api.v1.exception.UserException;
import com.goolbitg.api.v1.repository.ChallengeGroupRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeGroupRepository;
import com.goolbitg.api.v1.repository.ChallengeGroupStatsRepository;
import com.goolbitg.api.v1.repository.UserRepository;

/**
 * ChallengeGroupServiceImpl
 */
@Service
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    @Autowired
    private ChallengeGroupRepository challengeGroupRepository;
    @Autowired
    private ChallengeGroupRecordRepository challengeGroupRecordRepository;
    @Autowired
    private ChallengeGroupStatsRepository challengeGroupStatsRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ChallengeGroupRecordDto checkChallengeGroup(String userId, Long groupId) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkChallengeGroup'");
    }

    @Override
    @Transactional
    public ChallengeGroupDto createChallengeGroup(String userId, ChallengeGroupDto challengeGroupDto) throws Exception {
        if (!userRepository.existsById(userId))
            throw UserException.userNotExist(userId);

        ChallengeGroup group = ChallengeGroup.builder()
                .title(challengeGroupDto.getTitle())
                .ownerId(userId)
                .hashtags(String.join(",", challengeGroupDto.getHashtags()))
                .maxSize(challengeGroupDto.getMaxSize())
                .reward(challengeGroupDto.getReward())
                .isHidden(challengeGroupDto.getIsHidden())
                .password(challengeGroupDto.getPassword())
                .build();

        ChallengeGroup create = challengeGroupRepository.save(group);

        return getChallengeGroupDto(create);
    }

    private ChallengeGroupDto getChallengeGroupDto(ChallengeGroup create) {
        ChallengeGroupDto result = new ChallengeGroupDto();
        result.setId(create.getId());
        result.setTitle(create.getTitle());
        result.setOwnerId(create.getOwnerId());
        result.setHashtags(Arrays.asList(create.getHashtags().split(",")));
        result.setIsHidden(create.isHidden());
        result.setPeopleCount(create.getPeopleCount());
        result.setAvgAchieveRatio(create.getAvgAchieveRatio());
        result.setMaxAchieveDays(create.getMaxAchieveDays());
        return result;
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
