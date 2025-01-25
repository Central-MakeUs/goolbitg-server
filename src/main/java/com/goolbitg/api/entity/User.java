package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.goolbitg.api.model.Gender;

import lombok.Getter;
import lombok.Setter;

/**
 * User
 */
@Entity
@Table(name = "users")
@Getter
@Setter
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
    private String spendingTypeId;

    @Column(name = "allow_push_notification")
    private Boolean allowPushNotification;

    @Column(name = "agreement1")
    private Boolean agreement1;

    @Column(name = "agreement2")
    private Boolean agreement2;

    @Column(name = "agreement3")
    private Boolean agreement3;

    @Column(name = "agreement4")
    private Boolean agreement4;

}
