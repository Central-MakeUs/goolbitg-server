package com.goolbitg.api.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * UnregisterHistory
 */
@Entity
@Table(name = "unregister_histories")
@Getter
@Setter
public class UnregisterHistory {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "unregister_date")
    private LocalDate unregisterDate;

    @Column(name = "reason")
    private String reason;

}
