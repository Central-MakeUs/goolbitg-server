package com.goolbitg.api.v1.controller;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.BuyOrNotApi;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotReportRequest;
import com.goolbitg.api.model.BuyOrNotVoteChangeDto;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.PaginatedBuyOrNotDto;
import com.goolbitg.api.v1.security.AuthUtil;
import com.goolbitg.api.v1.service.BuyOrNotService;

import lombok.RequiredArgsConstructor;

/**
 * BuyOrNotController
 */
@RestController
@RequiredArgsConstructor
public class BuyOrNotController implements BuyOrNotApi {

    @Autowired
    private final BuyOrNotService buyOrNotService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<BuyOrNotDto> createBuyOrNot(@Valid BuyOrNotDto buyOrNotDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        BuyOrNotDto result = buyOrNotService.createBuyOrNot(userId, buyOrNotDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBuyOrNot(Long postId) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        buyOrNotService.deleteBuyOrNot(userId, postId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BuyOrNotDto> getBuyOrNot(Long postId) throws Exception {
        BuyOrNotDto result = buyOrNotService.getBuyOrNot(postId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PaginatedBuyOrNotDto> getBuyOrNots(@Valid Integer page, @Valid Integer size,
            @Valid Boolean created) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        String writerId = null;
        if (created) writerId = userId;
        PaginatedBuyOrNotDto result = buyOrNotService.getBuyOrNots(page, size, userId, writerId);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BuyOrNotDto> updateBuyOrNot(Long postId, @Valid BuyOrNotDto buyOrNotDto) throws Exception {
        String userId = AuthUtil.getLoginUserId();
        BuyOrNotDto result = buyOrNotService.updateBuyOrNot(userId, postId, buyOrNotDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BuyOrNotVoteChangeDto> voteBuyOrNot(Long postId, @Valid BuyOrNotVoteDto buyOrNotVoteDto)
            throws Exception {
        String userId = AuthUtil.getLoginUserId();
        BuyOrNotVoteChangeDto result = buyOrNotService.voteBuyOrNot(userId, postId, buyOrNotVoteDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> buyOrNotReport(Long postId, @Valid BuyOrNotReportRequest buyOrNotReportRequest)
            throws Exception {
        String userId = AuthUtil.getLoginUserId();
        buyOrNotService.reportBuyOrNot(userId, postId, buyOrNotReportRequest.getReason());
        return ResponseEntity.ok().build();
    }

}
