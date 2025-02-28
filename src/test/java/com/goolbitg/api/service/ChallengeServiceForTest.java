package com.goolbitg.api.service;

import java.time.LocalDate;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChallengeServiceForTest
 */
@Profile("test")
@Primary
@Service
public class ChallengeServiceForTest extends ChallengeServiceImpl {

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void failChallenge(String userId, Long challengeId, LocalDate date) {
        super.failChallenge(userId, challengeId, date);
    }

    
}
