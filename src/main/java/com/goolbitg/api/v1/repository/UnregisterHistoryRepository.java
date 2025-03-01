package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.UnregisterHistory;

/**
 * UnregisterHistoryRepository
 */
public interface UnregisterHistoryRepository extends JpaRepository<UnregisterHistory, String> {

    
}
