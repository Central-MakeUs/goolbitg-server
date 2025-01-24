package com.goolbitg.api.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.ChallengeRecord;
import com.goolbitg.api.entity.ChallengeRecordId;
import com.goolbitg.api.model.ChallengeRecordStatus;

/**
 * ChallengeRecordRepository
 */
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, ChallengeRecordId> {

    Page<ChallengeRecord> findAllByUserIdAndDate(Pageable pageable, String userId, LocalDate date);
    Page<ChallengeRecord> findAllByUserIdAndDateAndStatus(Pageable pageable, String userId, LocalDate date, ChallengeRecordStatus status);
}
