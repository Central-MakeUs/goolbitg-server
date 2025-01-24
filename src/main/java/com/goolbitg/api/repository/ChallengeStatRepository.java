package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.ChallengeStat;
import com.goolbitg.api.entity.ChallengeStatId;

/**
 * ChallengeStatRepository
 */
public interface ChallengeStatRepository extends JpaRepository<ChallengeStat, ChallengeStatId> {

    
}
