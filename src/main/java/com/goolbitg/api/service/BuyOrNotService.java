package com.goolbitg.api.service;

import java.util.List;

import com.goolbitg.api.entity.BuyOrNot;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotVoteChangeDto;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.PaginatedBuyOrNotDto;

/**
 * BuyOrNotService
 */
public interface BuyOrNotService {

    BuyOrNotDto getBuyOrNot(Long postId);
    PaginatedBuyOrNotDto getBuyOrNots(Integer page, Integer size, String userId, String writerId);
    BuyOrNotDto createBuyOrNot(String userId, BuyOrNotDto request);
    BuyOrNotDto updateBuyOrNot(String userId, Long postId, BuyOrNotDto request);
    void deleteBuyOrNot(String userId, Long postId);
    BuyOrNotVoteChangeDto voteBuyOrNot(String userId, Long postId, BuyOrNotVoteDto request);
    void reportBuyOrNot(String userId, Long postId, String reason);
    List<BuyOrNotDto> getTimeCompletedBuyOrNots();

}
