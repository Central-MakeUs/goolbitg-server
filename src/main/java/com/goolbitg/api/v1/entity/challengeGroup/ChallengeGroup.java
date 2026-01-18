package com.goolbitg.api.v1.entity.challengeGroup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.v1.entity.BaseEntity;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ChallengeGroup
 */
@Entity
@Table(name = "challenge_groups")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeGroup extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "hashtags")
    private String hashtags;

    @Column(name = "max_size")
    private int maxSize;

    @Column(name = "reward")
    private int reward;

    @Column(name = "people_count")
    private int peopleCount;

    @Column(name = "participant_count")
    private int participantCount;

    @Column(name = "is_hidden")
    private boolean isHidden;

    @Column(name = "password")
    private String password;

    @Column(name = "avg_achieve_ratio")
    private float avgAchieveRatio;

    @Column(name = "max_achieve_days")
    private int maxAchieveDays;

    @Column(name = "category")
    @Enumerated
    private Category category;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
