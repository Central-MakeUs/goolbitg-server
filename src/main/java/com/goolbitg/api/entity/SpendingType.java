package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;

/**
 * SpendingType
 */
@Entity
@Table(name = "spending_types")
@Getter
public class SpendingType {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "onboarding_result_url")
    private String onboardingResultUrl;

    @Column(name = "goal")
    private Integer goal;

}
