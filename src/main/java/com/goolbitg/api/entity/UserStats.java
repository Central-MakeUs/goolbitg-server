package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * UserStats
 */
@Entity
@Getter
@Setter
@Table(name = "user_stats")
public class UserStats {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "challenge_count")
    private Integer challengeCount;

    @Column(name = "post_count")
    private Integer postCount;

    @Column(name = "achivement_guage")
    private Integer achivementGuage;

}
