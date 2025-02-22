package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goolbitg.api.entity.ChallengeStat;
import com.goolbitg.api.entity.ChallengeStatId;

/**
 * ChallengeStatRepository
 */
public interface ChallengeStatRepository extends JpaRepository<ChallengeStat, ChallengeStatId> {

    @Query("""
        SELECT MAX(s.continueCount)
        FROM ChallengeStat s
        WHERE s.challengeId = :challengeId
        """)
    Integer getMaxContinueCountByChallengeId(@Param("challengeId") Long challengeId);

}
