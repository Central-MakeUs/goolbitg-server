package com.goolbitg.api.v1.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.v1.entity.challenge.ChallengeRecord;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupRecord;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupRecordId;

/**
 * ChallengeGroupRecordRepository
 */
public interface ChallengeGroupRecordRepository extends JpaRepository<ChallengeGroupRecord, ChallengeGroupRecordId> {

    Iterable<ChallengeGroupRecord> findAllByDateAndStatus(LocalDate yesterday, ChallengeRecordStatus status);

}
