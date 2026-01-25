package com.goolbitg.api.v1.entity.buyOrNot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.goolbitg.api.model.BuyOrNotVoteType;
import com.goolbitg.api.v1.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BuyOrNotVote
 */
@Entity
@Table(name = "buyornot_votes")
@IdClass(BuyOrNotVoteId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyOrNotVote extends BaseEntity {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "voter_id")
    private String voterId;

    @Setter
    @Column(name = "vote")
    @Enumerated(EnumType.STRING)
    private BuyOrNotVoteType vote;

    @Column(name = "writer_id")
    private String writerId;


    public static BuyOrNotVote getDefault(Long postId, String voterId) {
        return BuyOrNotVote.builder()
                .postId(postId)
                .voterId(voterId)
                .build();
    }

}
