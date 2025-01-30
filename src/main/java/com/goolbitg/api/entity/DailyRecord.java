package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.goolbitg.api.model.ChallengeRecordStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * DailyRecord
 */
@Entity
@Table(name = "daily_records")
@IdClass(DailyRecordId.class)
@Getter
@Setter
public class DailyRecord {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "saving")
    private Integer saving;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChallengeRecordStatus status;

}
