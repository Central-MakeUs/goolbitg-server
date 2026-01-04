package com.goolbitg.api.v1.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.ChallengeGroupApi;
import com.goolbitg.api.model.ChallengeGroupDto;
import com.goolbitg.api.model.ChallengeGroupEnrollmentDto;
import com.goolbitg.api.model.ChallengeGroupRankDto;
import com.goolbitg.api.model.ChallengeGroupRecordDto;
import com.goolbitg.api.model.ChallengeGroupTrippleDto;
import com.goolbitg.api.model.PaginatedChallengeGroupDto;
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
    public ResponseEntity<ChallengeGroupDto> createChallengeGroup(ChallengeGroupDto challengeGroupDto)
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
    public ResponseEntity<Void> enrollChallengeGroup(Long groupId, ChallengeGroupEnrollmentDto enrollment) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        String password = enrollment.getPassword();
        challengeGroupService.enrollChallengeGroup(userId, groupId, password);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ChallengeGroupRankDto> getChallengeGroup(Long groupId) throws Exception {
        ChallengeGroupRankDto result = challengeGroupService.getChallengeGroup(groupId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeGroupTrippleDto> getChallengeGroupTripple(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        return ResponseEntity.ok(challengeGroupService.getTripple(userId, groupId));
    }

    @Override
    public ResponseEntity<PaginatedChallengeGroupDto> getChallengeGroups(Integer page, Integer size,
        String search, Boolean created, Boolean participating) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        if (page == null) page = 0;
        if (size == null) size = 10;
        PaginatedChallengeGroupDto result = challengeGroupService.getChallengeGroups(userId, page, size, search, created, participating);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ChallengeGroupDto> updateChallengeGroup(Long groupId,
        ChallengeGroupDto challengeGroupDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        ChallengeGroupDto result = challengeGroupService.updateChallengeGroup(userId, groupId, challengeGroupDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> exitChallengeGroup(Long groupId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        challengeGroupService.exitChallengeGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
