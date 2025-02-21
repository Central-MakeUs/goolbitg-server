package com.goolbitg.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.BuyOrNotReport;
import com.goolbitg.api.entity.BuyOrNotReportId;

/**
 * BuyOrNotReportRepository
 */
public interface BuyOrNotReportRepository extends JpaRepository<BuyOrNotReport, BuyOrNotReportId> {

}
