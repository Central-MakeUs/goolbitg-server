package com.goolbitg.api.v1.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.v1.entity.challenge.ChallengeRecord;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupRecord;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupRecordId;

/**
 * ChallengeGroupRecordRepository
 */
public interface ChallengeGroupRecordRepository extends JpaRepository<ChallengeGroupRecord, ChallengeGroupRecordId> {

    Iterable<ChallengeGroupRecord> findAllByDateAndStatus(LocalDate yesterday, ChallengeRecordStatus status);

    @Query("""
        select count(cr)
        from ChallengeGroupRecord cr
        where cr.userId = :userId
        and cr.status = :status
        and cr.date between :startDate and :endDate
        """)
    int countByUserAndStatusAndDateRange(
        @Param("userId") String userId,
        @Param("status") ChallengeRecordStatus status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

}
