package com.goolbitg.api.v1.entity.challengeGroup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_group_stats")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChallengeGroupStatsId.class)
public class ChallengeGroupStats {

    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_count", nullable = false)
    @Builder.Default
    private int totalCount = 0;

    @Column(name = "saving", nullable = false)
    @Builder.Default
    private int saving = 0;

    public void increaseSaving(int amount) {
        saving += amount;
    }
}

