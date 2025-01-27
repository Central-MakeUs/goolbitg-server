package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.goolbitg.api.entity.UserStats;

/**
 * UserStatsRepository
 */
public interface UserStatsRepository extends JpaRepository<UserStats, String> {

}
