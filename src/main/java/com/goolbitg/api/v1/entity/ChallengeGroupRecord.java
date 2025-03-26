package com.goolbitg.api.v1.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

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

@Entity
@Table(name = "challenge_group_records")
@IdClass(ChallengeGroupRecordId.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeGroupRecord {

    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChallengeRecordStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChallengeGroupRecord)) return false;
        ChallengeGroupRecord that = (ChallengeGroupRecord) o;
        return Objects.equals(groupId, that.groupId) &&
               Objects.equals(userId, that.userId) &&
               Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId, date);
    }
}

