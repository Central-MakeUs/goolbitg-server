package com.goolbitg.api.v1.entity.challengeGroup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.goolbitg.api.v1.entity.BaseEntity;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.EnrollmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ChallengeGroupEnrollment
 */
@Entity
@Table(name = "challenge_group_enrollments")
@IdClass(ChallengeGroupEnrollmentId.class)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChallengeGroupEnrollment extends BaseEntity {

    @Id
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}

