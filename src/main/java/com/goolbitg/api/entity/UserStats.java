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
    private int challengeCount;

    @Column(name = "post_count")
    private int postCount;

    @Column(name = "achivement_guage")
    private float achivementGuage;

}
