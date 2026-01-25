package com.goolbitg.api.v1.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goolbitg.api.v1.entity.custom.BuyOrNotVoteAggregationCustom;
import com.goolbitg.api.v1.entity.custom.ChallengeRecordCustom;
import com.goolbitg.api.v1.repository.mappers.BuyOrNotVoteCustomMapper;
import com.goolbitg.api.v1.repository.mappers.ChallengeRecordCustomMapper;

/**
 * TestController
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final ChallengeRecordCustomMapper customMapper;
    private final BuyOrNotVoteCustomMapper buyOrNotVoteCustomMapper;

    @PostMapping("/test")
    public ResponseEntity<BuyOrNotVoteAggregationCustom> getTest(TestRequest body) {
        BuyOrNotVoteAggregationCustom result = buyOrNotVoteCustomMapper.aggregateVote("id0001", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 2, 1));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Data
    public static class TestRequest {
        private String message;
    }

}
