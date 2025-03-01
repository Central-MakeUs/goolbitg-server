package com.goolbitg.api.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * RegistrationToken
 */
@Entity
@Getter
@Table(name = "registration_tokens")
@NoArgsConstructor
public class RegistrationToken {

    @Id
    @Column(name = "registration_token")
    @NonNull
    private String registrationToken;

    @Column(name = "user_id")
    @NonNull
    private String userId;

    @Builder
    public RegistrationToken(String registrationToken, String userId) {
        this.registrationToken = registrationToken;
        this.userId = userId;
    }

}
