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

/**
 * User
 */
@Entity
@Table(name = "users")
@Getter
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

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

}
