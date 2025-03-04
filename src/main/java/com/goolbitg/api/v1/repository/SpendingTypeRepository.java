package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.SpendingType;

/**
 * SpendingTypeRepository
 */
public interface SpendingTypeRepository extends JpaRepository<SpendingType, Long> {

}
