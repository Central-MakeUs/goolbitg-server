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
        GROUP BY p.id
        HAVING COUNT(*) < 3
        """)
    Page<BuyOrNot> findAllFiltered(Pageable pageReq);

    @Query("""
        SELECT p
        FROM BuyOrNot p
        LEFT JOIN BuyOrNotReport r ON p.id = r.postId
        WHERE p.writerId = :writer_id
        GROUP BY p.id
        HAVING COUNT(p) < 3
        """)
    Page<BuyOrNot> findAllByWriterIdFiltered(@Param("writer_id") String writerId, Pageable pageReq);

}
