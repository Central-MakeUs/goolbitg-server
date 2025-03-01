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
        WHERE NOT EXISTS (
            SELECT 1 FROM BuyOrNotReport r2
            WHERE r2.postId = p.id
            AND r2.reporterId = :userId
        )
        AND (
            SELECT COUNT(*) FROM BuyOrNotReport r3
            WHERE r3.postId = p.id
        ) < 3
        ORDER BY p.id DESC
        """)
    Page<BuyOrNot> findAllFiltered(@Param("userId") String userId, Pageable pageReq);

    @Query("""
        SELECT p
        FROM BuyOrNot p
        LEFT JOIN BuyOrNotReport r1 ON p.id = r1.postId
        WHERE p.writerId = :writerId
        AND NOT EXISTS (
            SELECT 1 FROM BuyOrNotReport r2
            WHERE r2.postId = p.id
            AND r2.reporterId = :userId
        )
        AND (
            SELECT COUNT(*) FROM BuyOrNotReport r3
            WHERE r3.postId = p.id
        ) < 3
        ORDER BY p.id DESC
        """)
    Page<BuyOrNot> findAllByWriterIdFiltered(@Param("userId") String userId, @Param("writerId") String writerId, Pageable pageReq);

}
