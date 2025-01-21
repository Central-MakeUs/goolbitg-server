package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.model.ChallengeRecordStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * ChallengeRecord
 */
@Entity
@Table(name = "challenge_records")
@Getter
@Setter
public class ChallengeRecord {

    @Id
    @Column(name = "challenge_id")
    private Long challengeId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChallengeRecordStatus status;

}
