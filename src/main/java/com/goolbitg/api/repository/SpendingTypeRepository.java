package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.SpendingType;

/**
 * SpendingTypeRepository
 */
public interface SpendingTypeRepository extends JpaRepository<SpendingType, String> {

}
