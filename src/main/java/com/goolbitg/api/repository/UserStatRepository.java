package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.UserStat;

/**
 * UserStatsRepository
 */
public interface UserStatRepository extends JpaRepository<UserStat, String> {

}
