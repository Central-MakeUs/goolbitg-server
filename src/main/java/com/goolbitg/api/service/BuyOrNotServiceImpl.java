package com.goolbitg.api.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goolbitg.api.entity.BuyOrNot;
import com.goolbitg.api.entity.BuyOrNotVote;
import com.goolbitg.api.entity.BuyOrNotVoteId;
import com.goolbitg.api.exception.BuyOrNotException;
import com.goolbitg.api.model.BuyOrNotDto;
import com.goolbitg.api.model.BuyOrNotVoteChangeDto;
import com.goolbitg.api.model.BuyOrNotVoteDto;
import com.goolbitg.api.model.BuyOrNotVoteType;
import com.goolbitg.api.model.PaginatedBuyOrNotDto;
import com.goolbitg.api.repository.BuyOrNotRepository;
import com.goolbitg.api.repository.BuyOrNotVoteRepository;
import com.goolbitg.api.security.AuthUtil;

import lombok.RequiredArgsConstructor;

/**
 * BuyOrNotServiceImpl
 */
@Service
@RequiredArgsConstructor
public class BuyOrNotServiceImpl implements BuyOrNotService {

    @Autowired
    private final BuyOrNotRepository buyOrNotRepository;
    @Autowired
    private final BuyOrNotVoteRepository buyOrNotVoteRepository;

    @Override
    public BuyOrNotDto getBuyOrNot(Long postId) {
        BuyOrNot post = buyOrNotRepository.findById(postId)
                .orElseThrow(() -> BuyOrNotException.postNotExist(postId));

        return getBuyOrNotDto(post);
    }

    private BuyOrNotDto getBuyOrNotDto(BuyOrNot post) {
        int goodCount = buyOrNotVoteRepository.getCountOf(post.getId(), BuyOrNotVoteType.GOOD);
        int badCount = buyOrNotVoteRepository.getCountOf(post.getId(), BuyOrNotVoteType.BAD);

        BuyOrNotDto dto = new BuyOrNotDto();
        dto.setId(post.getId());
        dto.setWriterId(post.getWriterId());
        dto.setProductName(post.getProductName());
        dto.setProductPrice(post.getProductPrice());
        dto.setProductImageUrl(URI.create(post.getProductImageUrl()));
        dto.setGoodReason(post.getGoodReason());
        dto.setBadReason(post.getBadReason());
        dto.setGoodVoteCount(goodCount);
        dto.setBadVoteCount(badCount);
        return dto;
    }

    @Override
    public PaginatedBuyOrNotDto getBuyOrNots(Integer page, Integer size, Boolean created) {
        Pageable pageReq = PageRequest.of(page, size);
        Page<BuyOrNot> result;
        if (created) {
            result = buyOrNotRepository.findAllByWriterId(AuthUtil.getLoginUserId(), pageReq);
        } else {
            result = buyOrNotRepository.findAll(pageReq);
        }

        return getPaginatedBuyOrNotDto(result);
    }

    private PaginatedBuyOrNotDto getPaginatedBuyOrNotDto(Page<BuyOrNot> result) {
        PaginatedBuyOrNotDto dto = new PaginatedBuyOrNotDto();
        dto.setTotalPages(result.getTotalPages());
        dto.setTotalSize((int)result.getTotalElements());
        dto.setPage(result.getNumber());
        dto.setSize(result.getSize());
        dto.setItems(result.get().map(e -> getBuyOrNotDto(e)).toList());
        return dto;
    }

    @Override
    @Transactional
    public BuyOrNotDto createBuyOrNot(BuyOrNotDto request) {
        BuyOrNot post = new BuyOrNot();
        post.setWriterId(AuthUtil.getLoginUserId());
        post.setProductName(request.getProductName());
        post.setProductPrice(request.getProductPrice());
        post.setProductImageUrl(request.getProductImageUrl().toString());
        post.setGoodReason(request.getGoodReason());
        post.setBadReason(request.getBadReason());
        BuyOrNot created = buyOrNotRepository.save(post);

        return getBuyOrNotDto(created);
    }

    @Override
    @Transactional
    public BuyOrNotDto updateBuyOrNot(Long postId, BuyOrNotDto request) {
        BuyOrNot post = buyOrNotRepository.findById(postId)
                .orElseThrow(() -> BuyOrNotException.postNotExist(postId));
        if (request.getProductName() != null)
            post.setProductName(request.getProductName());
        if (request.getProductPrice() != null)
            post.setProductPrice(request.getProductPrice());
        if (request.getProductImageUrl() != null)
            post.setProductImageUrl(request.getProductImageUrl().toString());
        if (request.getGoodReason() != null)
            post.setGoodReason(request.getGoodReason());
        if (request.getBadReason() != null)
            post.setBadReason(request.getBadReason());
        BuyOrNot updated = buyOrNotRepository.save(post);

        return getBuyOrNotDto(updated);
    }

    @Override
    @Transactional
    public void deleteBuyOrNot(Long postId) {
        if (buyOrNotRepository.existsById(postId) == false)
            throw BuyOrNotException.postNotExist(postId);
        buyOrNotRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public BuyOrNotVoteChangeDto voteBuyOrNot(Long postId, BuyOrNotVoteDto request) {
        if (buyOrNotRepository.existsById(postId) == false)
            throw BuyOrNotException.postNotExist(postId);
        String voterId = AuthUtil.getLoginUserId();
        BuyOrNotVoteId id = new BuyOrNotVoteId(postId, voterId);
        BuyOrNotVote vote = buyOrNotVoteRepository.findById(id)
                .orElseGet(() -> {
            BuyOrNotVote newVote = new BuyOrNotVote();
            newVote.setPostId(postId);
            newVote.setVoterId(voterId);
            return newVote;
        });
        vote.setVote(request.getVote());
        buyOrNotVoteRepository.save(vote);

        return getBuyOrNotVoteChangeDto(postId);
    }

    private BuyOrNotVoteChangeDto getBuyOrNotVoteChangeDto(Long postId) {
        BuyOrNotVoteChangeDto dto = new BuyOrNotVoteChangeDto();
        int goodCount = buyOrNotVoteRepository.getCountOf(postId, BuyOrNotVoteType.GOOD);
        int badCount = buyOrNotVoteRepository.getCountOf(postId, BuyOrNotVoteType.BAD);
        dto.setGoodVoteCount(goodCount);
        dto.setBadVoteCount(badCount);
        return dto;
    }

}
