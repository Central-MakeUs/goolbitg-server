package com.goolbitg.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goolbitg.api.entity.BuyOrNot;

/**
 * BuyOrNotRepository
 */
public interface BuyOrNotRepository extends JpaRepository<BuyOrNot, Long> {

    @Query("""
        SELECT p
        FROM BuyOrNot p
        LEFT JOIN BuyOrNotReport r ON p.id = r.postId
        WHERE p.id NOT IN (
            SELECT r.postId FROM BuyOrNotReport r
            WHERE r.reporterId = :userId
        )
        GROUP BY p.id
        HAVING COUNT(*) < 3
        ORDER BY p.id DESC
        """)
    Page<BuyOrNot> findAllFiltered(@Param("userId") String userId, Pageable pageReq);

    @Query("""
        SELECT p
        FROM BuyOrNot p
        LEFT JOIN BuyOrNotReport r ON p.id = r.postId
        WHERE p.writerId = :writerId
        AND p.id NOT IN (
            SELECT r.postId FROM BuyOrNotReport r
            WHERE r.reporterId = :writerId
        )
        GROUP BY p.id
        HAVING COUNT(p) < 3
        ORDER BY p.id DESC
        """)
    Page<BuyOrNot> findAllByWriterIdFiltered(@Param("writerId") String writerId, Pageable pageReq);

}
