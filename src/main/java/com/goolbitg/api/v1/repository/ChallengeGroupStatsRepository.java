package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.ChallengeGroup;
import com.goolbitg.api.v1.entity.ChallengeGroupStats;
import com.goolbitg.api.v1.entity.ChallengeGroupStatsId;

/**
 * ChallengeGroupStatsRepository
 */
public interface ChallengeGroupStatsRepository extends JpaRepository<ChallengeGroupStats, ChallengeGroupStatsId> {

}
