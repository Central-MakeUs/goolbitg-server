package com.goolbitg.api.etc;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.goolbitg.api.integration.CustomIntegrationTest;
import com.goolbitg.api.repository.ChallengeRepository;

/**
 * SandboxTest
 */
@CustomIntegrationTest
public class SandboxTest {

    @Autowired
    private ChallengeRepository challengeRepository;

    //@Test
    //void aggregation_test() {
    //    List<Long> result = challengeRepository.getCountOfRecordsForEachChallenge();
    //    System.out.println(result);
    //}
    //
    //@Test
    //void find_all_join_with_record() {
    //    Pageable pageReq = PageRequest.of(0, 10);
    //    Page<Object[]> result = challengeRepository.findAllBySpendingTypeId("st01", pageReq);
    //    for (Object[] row : result) {
    //        System.out.println(((Challenge)row[0]).getTitle());
    //        System.out.println(((Long)row[1]));
    //    }
    //}
}
