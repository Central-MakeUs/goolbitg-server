package com.goolbitg.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * BuyOrNotReport
 */
@Entity
@Table(name = "buyornot_reports")
@IdClass(BuyOrNotReportId.class)
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class BuyOrNotReport {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Id
    @Column(name = "reporter_id")
    private String reporterId;

    @Column(name = "reason")
    private String reason;

}
