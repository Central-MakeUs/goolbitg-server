package com.goolbitg.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goolbitg.api.entity.Challenge;
import com.goolbitg.api.entity.SpendingType;

/**
 * ChallengeRepository
 */
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("""
        SELECT c
        FROM Challenge c
        JOIN ChallengeStat s ON c.id = s.challengeId
        JOIN User u ON s.userId = u.id
        WHERE u.spendingType = :spendingType
        GROUP BY c
        ORDER BY SUM(s.enrollCount) DESC
        """)
    Page<Challenge> findAllBySpendingTypeId(@Param("spendingType") SpendingType spendingTypeId, Pageable pageable);

    @Query("SELECT c.id FROM Challenge c")
    Iterable<Long> findAllIds();

}
