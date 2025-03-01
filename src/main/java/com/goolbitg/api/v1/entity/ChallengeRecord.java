package com.goolbitg.api.v1.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.goolbitg.api.model.ChallengeRecordStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ChallengeRecord
 */
@Entity
@Table(name = "challenge_records")
@IdClass(ChallengeRecordId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeRecord {

    @Id
    @Column(name = "challenge_id")
    private Long challengeId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChallengeRecordStatus status;

    @Column(name = "location")
    private Integer location;

    public void success() {
        if (status != ChallengeRecordStatus.WAIT)
            throw new IllegalStateException("challenge status is not wait");
        status = ChallengeRecordStatus.SUCCESS;
    }

    public void fail() {
        if (status != ChallengeRecordStatus.WAIT)
            throw new IllegalStateException("challenge status is not wait");
        status = ChallengeRecordStatus.FAIL;
    }

    public static ChallengeRecord getEmpty() {
        return ChallengeRecord.builder()
                .status(ChallengeRecordStatus.FAIL)
                .build();
    }
}
