package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.ChallengeGroupRecord;
import com.goolbitg.api.v1.entity.ChallengeGroupRecordId;

/**
 * ChallengeGroupRecordRepository
 */
public interface ChallengeGroupRecordRepository extends JpaRepository<ChallengeGroupRecord, ChallengeGroupRecordId> {

}
