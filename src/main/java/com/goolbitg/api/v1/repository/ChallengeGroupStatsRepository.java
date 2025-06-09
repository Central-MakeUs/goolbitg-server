package com.goolbitg.api.v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupStats;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupStatsId;

/**
 * ChallengeGroupStatsRepository
 */
public interface ChallengeGroupStatsRepository extends JpaRepository<ChallengeGroupStats, ChallengeGroupStatsId> {

    List<ChallengeGroupStats> findByGroupIdOrderBySavingDesc(Long groupId);

}
