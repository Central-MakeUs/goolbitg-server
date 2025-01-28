package com.goolbitg.api.service;

import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotVoteChangeDto;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.PaginatedBuyOrNotDto;

/**
 * BuyOrNotService
 */
public interface BuyOrNotService {

    BuyOrNotDto getBuyOrNot(Long postId);
    PaginatedBuyOrNotDto getBuyOrNots(Integer page, Integer size, Boolean created);
    BuyOrNotDto createBuyOrNot(BuyOrNotDto request);
    BuyOrNotDto updateBuyOrNot(Long postId, BuyOrNotDto request);
    void deleteBuyOrNot(Long postId);
    BuyOrNotVoteChangeDto voteBuyOrNot(Long postId, BuyOrNotVoteDto request);

}
