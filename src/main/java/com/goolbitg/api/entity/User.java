package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "apple_id")
    private String appleId;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "spending_type_id")
    private Long spendingTypeId;

    @Column(name = "allow_push_notification")
    private Boolean pushNotificationAgreement;

    @Column(name = "agreement1")
    private Boolean agreement1;

    @Column(name = "agreement2")
    private Boolean agreement2;

    @Column(name = "agreement3")
    private Boolean agreement3;

    @Column(name = "agreement4")
    private Boolean agreement4;

    public void setSpendingTypeId(Long spendingTypeId) {
        this.spendingTypeId = spendingTypeId;
    }

    public void updateInfo(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updateAgreement(
        boolean agreement1,
        boolean agreement2,
        boolean agreement3,
        boolean agreement4
    ) {
        this.agreement1 = agreement1;
        this.agreement2 = agreement2;
        this.agreement3 = agreement3;
        this.agreement4 = agreement4;
    }

    public void allowPushNotification() {
        pushNotificationAgreement = true;
    }
}
