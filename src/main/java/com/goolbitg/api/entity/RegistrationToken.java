package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;

/**
 * RegistrationToken
 */
@Entity
@Getter
@Table(name = "registration_tokens")
public class RegistrationToken {

    @Id
    @Column(name = "registration_token")
    private String RegistrationToken;

    @Column(name = "user_id")
    private String userId;

}
