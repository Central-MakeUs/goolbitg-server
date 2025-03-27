package com.goolbitg.api.v1.controller;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.ChallengeGroupApi;
import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.model.ChallengeGroupRecordDto;
import com.goolbitg.api.model.ChallengeGroupStatDto;
import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.model.PaginatedChallengeGroupDto;
import com.goolbitg.api.model.PaginatedChallengeGroupRecordDto;
import com.goolbitg.api.v1.security.AuthUtil;
import com.goolbitg.api.v1.service.ChallengeGroupService;

/**
 * ChallengeGroupController
 */
@RestController
public class ChallengeGroupController implements ChallengeGroupApi {

    @Autowired
    private ChallengeGroupService challengeGroupService;

    @Override
    public ResponseEntity<ChallengeGroupRecordDto> checkChallengeGroup(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupRecordDto result = challengeGroupService.checkChallengeGroup(userId, groupId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeGroupDto> createChallengeGroup(@Valid ChallengeGroupDto challengeGroupDto)
    throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupDto result = challengeGroupService.createChallengeGroup(userId, challengeGroupDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteChallengeGroup(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        challengeGroupService.deleteChallengeGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> enrollChallengeGroup(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        challengeGroupService.enrollChallengeGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ChallengeGroupDto> getChallengeGroup(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupDto result = challengeGroupService.getChallengeGroup(userId, groupId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeGroupRecordDto> getChallengeGroupRecord(Long groupId, @Valid LocalDate date)
    throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupRecordDto result = challengeGroupService.getChallengeGroupRecord(userId, groupId, date);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaginatedChallengeGroupRecordDto> getChallengeGroupRecords(@Valid Integer page,
        @Valid Integer size, @Valid LocalDate date, @Valid ChallengeRecordStatus status, @Valid Boolean created)
    throws Exception {
        String userId = AuthUtil.getLoginUserId();
        PaginatedChallengeGroupRecordDto result = challengeGroupService.getChallengeGroupRecords(userId, page, size, date, status, created);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeGroupStatDto> getChallengeGroupStat(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupStatDto result = challengeGroupService.getChallengeGroupStat(userId, groupId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaginatedChallengeGroupDto> getChallengeGroups(@Valid Integer page, @Valid Integer size,
        @Valid String search, @Valid Boolean created) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        PaginatedChallengeGroupDto result = challengeGroupService.getChallengeGroups(userId, page, size, search, created);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeGroupDto> updateChallengeGroup(Long groupId,
        @Valid ChallengeGroupDto challengeGroupDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupDto result = challengeGroupService.updateChallengeGroup(userId, groupId, challengeGroupDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
