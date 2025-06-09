package com.goolbitg.api.v1.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.model.ChallengeGroupRankDto;
import com.goolbitg.api.model.ChallengeGroupRankDtoRankInner;
import com.goolbitg.api.model.ChallengeGroupRecordDto;
import com.goolbitg.api.model.ChallengeGroupStatDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.PaginatedChallengeGroupDto;
import com.goolbitg.api.model.PaginatedChallengeGroupRecordDto;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroup;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupEnrollment;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupEnrollmentId;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupRecord;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupRecordId;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupStats;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupStatsId;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.EnrollmentStatus;
import com.goolbitg.api.v1.entity.user.User;
import com.goolbitg.api.v1.exception.ChallengeException;
import com.goolbitg.api.v1.exception.UserException;
import com.goolbitg.api.v1.repository.ChallengeGroupEnrollmentRepository;
import com.goolbitg.api.v1.repository.ChallengeGroupRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeGroupRepository;
import com.goolbitg.api.v1.repository.ChallengeGroupStatsRepository;
import com.goolbitg.api.v1.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * ChallengeGroupServiceImpl
 */
@Slf4j
@Service
public class ChallengeGroupServiceImpl implements ChallengeGroupService {

    @Autowired
    private ChallengeGroupRepository challengeGroupRepository;
    @Autowired
    private ChallengeGroupRecordRepository challengeGroupRecordRepository;
    @Autowired
    private ChallengeGroupStatsRepository challengeGroupStatsRepository;
    @Autowired
    private ChallengeGroupEnrollmentRepository ChallengeGroupEnrollmentRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ChallengeGroupRecordDto checkChallengeGroup(String userId, Long groupId) throws Exception {
        validateUser(userId);
        ChallengeGroup group = getOrThrowChallengeGroup(groupId);
        ChallengeGroupRecordId id = new ChallengeGroupRecordId(groupId, userId, LocalDate.now());

        Optional<ChallengeGroupRecord> result = challengeGroupRecordRepository.findById(id);
        ChallengeGroupRecord record;
        if (result.isPresent()) {
            record = result.get();
            record.setStatus(ChallengeRecordStatus.SUCCESS);
        } else {
            record = ChallengeGroupRecord.builder()
                    .userId(userId)
                    .groupId(groupId)
                    .date(LocalDate.now())
                    .status(ChallengeRecordStatus.SUCCESS)
                    .build();
            challengeGroupRecordRepository.save(record);
        }
        ChallengeGroupStatsId statsId = new ChallengeGroupStatsId(groupId, userId);
        ChallengeGroupStats stats = challengeGroupStatsRepository.findById(statsId)
                .orElseThrow(() -> ChallengeException.notEnrolled(groupId));
        stats.increaseSaving(group.getReward());

        return getChallengeGroupRecordDto(record);
    }

    private ChallengeGroupRecordDto getChallengeGroupRecordDto(ChallengeGroupRecord record) {
        ChallengeGroupRecordDto dto = new ChallengeGroupRecordDto();
        dto.setUserId(record.getUserId());
        dto.setChallengeGroupId(record.getGroupId());
        dto.setDate(record.getDate());
        dto.setStatus(record.getStatus());
        return dto;
    }

    @Override
    @Transactional
    public ChallengeGroupDto createChallengeGroup(String userId, ChallengeGroupDto challengeGroupDto) throws Exception {
        validateUser(userId);

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
    @Transactional
    public void deleteChallengeGroup(String userId, Long groupId) throws Exception {
        validateUser(userId);
        ChallengeGroup group = getOrThrowChallengeGroup(groupId);
        if (group.getOwnerId().equals(userId))
            challengeGroupRepository.delete(group);
    }

    @Override
    @Transactional
    public void enrollChallengeGroup(String userId, Long groupId) throws Exception {
        validateUser(userId);
        ChallengeGroup group = getOrThrowChallengeGroup(groupId);
        ChallengeGroupEnrollmentId id = new ChallengeGroupEnrollmentId(group.getId(), userId);
        Optional<ChallengeGroupEnrollment> result = ChallengeGroupEnrollmentRepository.findById(id);

        ChallengeGroupEnrollment enrollment;
        if (result.isPresent()) {
            if (result.get().getStatus().equals(EnrollmentStatus.ENROLL))
                throw ChallengeException.alreadyEnrolled(group.getId());

            enrollment = result.get();
            enrollment.setStatus(EnrollmentStatus.ENROLL);
        } else {
            enrollment = ChallengeGroupEnrollment.builder()
                    .groupId(group.getId())
                    .userId(userId)
                    .status(EnrollmentStatus.ENROLL)
                    .build();
            ChallengeGroupStats stats = ChallengeGroupStats.builder()
                    .userId(userId)
                    .groupId(group.getId())
                    .build();
            challengeGroupStatsRepository.save(stats);
        }
        
        ChallengeGroupEnrollmentRepository.save(enrollment);
    }

    @Override
    public ChallengeGroupRankDto getChallengeGroup(Long groupId) throws Exception {
        ChallengeGroup group = getOrThrowChallengeGroup(groupId);
        List<ChallengeGroupStats> result = challengeGroupStatsRepository.findByGroupIdOrderBySavingDesc(groupId);

        ChallengeGroupRankDto dto = new ChallengeGroupRankDto();
        dto.setGroup(getChallengeGroupDto(group));
        dto.setRank(result.stream().map(stats -> {
            ChallengeGroupRankDtoRankInner rankInner = new ChallengeGroupRankDtoRankInner();
            User user = userRepository.findById(stats.getUserId()).get();
            rankInner.setName(user.getNickname());
            rankInner.setSaving(stats.getSaving());
            rankInner.setProfileUrl(user.getSpendingType().getProfileUrl());
            return rankInner;
        }).toList());

        return dto;
    }

    private void validateUser(String userId) {
        if (!userRepository.existsById(userId))
            throw UserException.userNotExist(userId);
    }

    @Override
    public ChallengeGroupRecordDto getChallengeGroupRecord(String userId, Long groupId, LocalDate date)
            throws Exception {
        validateUser(userId);
        getOrThrowChallengeGroup(groupId);
        ChallengeGroupRecordId id = new ChallengeGroupRecordId(groupId, userId, date);
        ChallengeGroupRecord record = getOrCreateChallengeGroupRecord(id);

        return getChallengeGroupRecordDto(record);
    }

    private ChallengeGroupRecord getOrCreateChallengeGroupRecord(ChallengeGroupRecordId id) {
        Optional<ChallengeGroupRecord> result = challengeGroupRecordRepository.findById(id);
        ChallengeGroupRecord record;
        if (result.isPresent()) {
            record = result.get();
            record.setStatus(ChallengeRecordStatus.SUCCESS);
        } else {
            record = ChallengeGroupRecord.builder()
                    .userId(id.userId())
                    .groupId(id.groupId())
                    .date(LocalDate.now())
                    .status(ChallengeRecordStatus.SUCCESS)
                    .build();
            challengeGroupRecordRepository.save(record);
        }
        return record;
    }

    @Override
    public PaginatedChallengeGroupRecordDto getChallengeGroupRecords(String userId, int page, int size,
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
    public PaginatedChallengeGroupDto getChallengeGroups(String userId, int page, int size, String search,
            Boolean created) throws Exception {
        Pageable pageReq = PageRequest.of(page, size);

        Page<ChallengeGroup> result;
        if (search == null || search.isBlank()) {
            if (created) {
                result = challengeGroupRepository.findByOwnerId(userId, pageReq);
            } else {
                result = challengeGroupRepository.findAll(pageReq);
            }
        } else {
            if (created) {
                result = challengeGroupRepository.findByTitleContainingOrHashtagsContainingAndOwnerId(search, search, userId, pageReq);
            } else {
                result = challengeGroupRepository.findByTitleContainingOrHashtagsContaining(search, search, pageReq);
            }
        }

        PaginatedChallengeGroupDto dto = getDto(result);
        return dto;
    }

    private PaginatedChallengeGroupDto getDto(Page<ChallengeGroup> result) {
        PaginatedChallengeGroupDto dto = new PaginatedChallengeGroupDto();
        dto.setTotalSize((int)result.getTotalElements());
        dto.setTotalPages(result.getTotalPages());
        dto.setSize(result.getNumberOfElements());
        dto.setPage(result.getNumber());
        dto.setItems(result.getContent().stream()
                .map(this::getChallengeGroupDto)
                .toList());
        return dto;
    }

    @Override
    @Transactional
    public ChallengeGroupDto updateChallengeGroup(String userId, Long groupId, ChallengeGroupDto challengeGroupDto)
            throws Exception {
        validateUser(userId);

        ChallengeGroup group = getOrThrowChallengeGroup(groupId);

        updateChallengGroupInner(challengeGroupDto, group);

        return getChallengeGroupDto(group);
    }

    private ChallengeGroup getOrThrowChallengeGroup(Long groupId) {
        ChallengeGroup group = challengeGroupRepository.findById(groupId)
                .orElseThrow(() -> ChallengeException.challengeNotExist(groupId));
        return group;
    }

    private void updateChallengGroupInner(ChallengeGroupDto challengeGroupDto, ChallengeGroup group) {
        Integer maxSize = challengeGroupDto.getMaxSize();
        if (maxSize != null && maxSize >= group.getPeopleCount())
            group.setMaxSize(challengeGroupDto.getMaxSize());
        if (challengeGroupDto.getTitle() != null)
            group.setTitle(challengeGroupDto.getTitle());
        if (challengeGroupDto.getReward() != null)
            group.setReward(challengeGroupDto.getReward());
        if (challengeGroupDto.getIsHidden() != null)
            group.setHidden(challengeGroupDto.getIsHidden());
        if (challengeGroupDto.getPassword() != null)
            group.setPassword(challengeGroupDto.getPassword());
        if (challengeGroupDto.getHashtags() != null)
            group.setHashtags(String.join(",", challengeGroupDto.getHashtags()));
    }

}
