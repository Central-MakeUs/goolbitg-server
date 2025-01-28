package com.goolbitg.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.BuyOrNot;

/**
 * BuyOrNotRepository
 */
public interface BuyOrNotRepository extends JpaRepository<BuyOrNot, Long> {

    Page<BuyOrNot> findAllByWriterId(String loginUserId, Pageable pageReq);

}
