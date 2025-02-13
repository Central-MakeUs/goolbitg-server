package com.goolbitg.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goolbitg.api.entity.ChallengeRecord;
import com.goolbitg.api.entity.ChallengeRecordId;
import com.goolbitg.api.model.ChallengeRecordStatus;

/**
 * ChallengeRecordRepository
 */
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, ChallengeRecordId> {

    Page<ChallengeRecord> findAllByUserIdAndDate(Pageable pageable, String userId, LocalDate date);
    Page<ChallengeRecord> findAllByUserIdAndDateAndStatus(Pageable pageable, String userId, LocalDate date, ChallengeRecordStatus status);
    Iterable<ChallengeRecord> findAllByDateAndStatus(LocalDate yesterday, ChallengeRecordStatus status);
    int countByChallengeIdAndDate(Long challengeId, LocalDate date);
    int countByUserIdAndDate(String id, LocalDate today);

    @Query("""
        SELECT r
        FROM ChallengeRecord r
        WHERE userId = :userId
        AND date = :date
        AND status = 'WAIT'
        """)
    List<ChallengeRecord> findAllIncompletedRecords(@Param("userId") String userId, @Param("date") LocalDate date);
    void deleteByUserId(String userId);
}
