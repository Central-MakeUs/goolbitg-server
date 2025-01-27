package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.goolbitg.api.model.BuyOrNotVoteType;

import lombok.Getter;
import lombok.Setter;

/**
 * BuyOrNotVote
 */
@Entity
@Table(name = "buyornot_votes")
@IdClass(BuyOrNotVoteId.class)
@Getter
@Setter
// TODO: extends base entity
public class BuyOrNotVote {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "voter_id")
    private String voterId;

    @Column(name = "vote")
    @Enumerated(EnumType.STRING)
    private BuyOrNotVoteType vote;

}
