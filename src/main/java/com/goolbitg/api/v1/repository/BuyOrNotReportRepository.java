package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.BuyOrNotReport;
import com.goolbitg.api.v1.entity.BuyOrNotReportId;

/**
 * BuyOrNotReportRepository
 */
public interface BuyOrNotReportRepository extends JpaRepository<BuyOrNotReport, BuyOrNotReportId> {

}
