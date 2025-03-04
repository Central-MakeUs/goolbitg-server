package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goolbitg.api.v1.entity.BuyOrNotVote;
import com.goolbitg.api.v1.entity.BuyOrNotVoteId;
import com.goolbitg.api.model.BuyOrNotVoteType;

/**
 * BuyOrNotVoteRepository
 */
public interface BuyOrNotVoteRepository extends JpaRepository<BuyOrNotVote, BuyOrNotVoteId> {

    @Query("""
        SELECT COUNT(v)
        FROM BuyOrNotVote v
        WHERE v.postId = :postId
        AND v.vote = :vote
        """)
    Integer getCountOf(@Param("postId") Long postId, @Param("vote") BuyOrNotVoteType vote);

}
