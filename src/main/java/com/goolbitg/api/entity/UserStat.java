package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserStat
 */
@Entity
@Table(name = "user_stats")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStat {

    @Id
    @Column(name = "user_id")
    private String userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "challenge_count")
    private Integer challengeCount;

    @Column(name = "post_count")
    private Integer postCount;

    @Column(name = "achievement_guage")
    private Integer achievementGuage;

    @Column(name = "continue_count")
    private Integer continueCount;

    public void setAchievementGuage(int achievementGuage) {
        this.achievementGuage = achievementGuage;
    }

    public void increaseChallengeCount() {
        challengeCount += 1;
    }

    public void increaseContinueCount() {
        continueCount += 1;
    }

    public void resetContinueCount() {
        continueCount = 0;
    }

    public static UserStat getDefault(User user) {
        return UserStat.builder()
                .user(user)
                .challengeCount(0)
                .postCount(0)
                .achievementGuage(0)
                .continueCount(0)
                .build();
    }

}
