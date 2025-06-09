package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupEnrollment;
import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroupEnrollmentId;

/**
 * ChallengeGroupEnrollmentRepository
 */
public interface ChallengeGroupEnrollmentRepository extends JpaRepository<ChallengeGroupEnrollment, ChallengeGroupEnrollmentId> {

    
}
