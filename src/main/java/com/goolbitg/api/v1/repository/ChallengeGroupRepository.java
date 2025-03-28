package com.goolbitg.api.v1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.ChallengeGroup;

/**
 * ChallengeGroupRepository
 */
public interface ChallengeGroupRepository extends JpaRepository<ChallengeGroup, Long> {
    Page<ChallengeGroup> findAll(Pageable pageable);
    Page<ChallengeGroup> findByTitleContainingOrHashtagsContaining(String title, String hashtags, Pageable pageable);
    Page<ChallengeGroup> findByOwnerId(String ownerId, Pageable pageable);
    Page<ChallengeGroup> findByTitleContainingOrHashtagsContainingAndOwnerId(String title, String hashtags, String ownerId, Pageable pageable);
}
