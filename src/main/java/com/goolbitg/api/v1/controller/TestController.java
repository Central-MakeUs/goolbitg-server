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
import com.goolbitg.api.v1.repository.mappers.UserStatCustomMapper;

/**
 * TestController
 */
@RestController
@RequiredArgsConstructor
public class TestController {

    private final ChallengeRecordCustomMapper customMapper;
    private final BuyOrNotVoteCustomMapper buyOrNotVoteCustomMapper;
    private final UserStatCustomMapper userStatCustomMapper;

    @PostMapping("/test")
    public ResponseEntity<Integer> getTest(TestRequest body) {
        // Integer rank = userStatCustomMapper.getRankOfSpendingType("id0005", 5);
        Integer totalCount = userStatCustomMapper.getTotalCountOfSpendingType(5);
        return new ResponseEntity<>(totalCount, HttpStatus.OK);
    }

    @Data
    public static class TestRequest {
        private String message;
    }

}
