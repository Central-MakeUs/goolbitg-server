package com.goolbitg.api.controller;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.BuyOrNotApi;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotVoteChangeDto;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.PaginatedBuyOrNotDto;
import com.goolbitg.api.service.BuyOrNotService;

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
        BuyOrNotDto result = buyOrNotService.createBuyOrNot(buyOrNotDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBuyOrNot(Long postId) throws Exception {
        buyOrNotService.deleteBuyOrNot(postId);
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
        PaginatedBuyOrNotDto result = buyOrNotService.getBuyOrNots(page, size, created);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BuyOrNotDto> updateBuyOrNot(Long postId, @Valid BuyOrNotDto buyOrNotDto) throws Exception {
        BuyOrNotDto result = buyOrNotService.updateBuyOrNot(postId, buyOrNotDto);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BuyOrNotVoteChangeDto> voteBuyOrNot(Long postId, @Valid BuyOrNotVoteDto buyOrNotVoteDto)
            throws Exception {
        BuyOrNotVoteChangeDto result = buyOrNotService.voteBuyOrNot(postId, buyOrNotVoteDto);
        return ResponseEntity.ok(result);
    }

}
