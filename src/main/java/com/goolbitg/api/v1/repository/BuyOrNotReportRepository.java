package com.goolbitg.api.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.buyOrNot.BuyOrNotReport;
import com.goolbitg.api.v1.entity.buyOrNot.BuyOrNotReportId;

/**
 * BuyOrNotReportRepository
 */
public interface BuyOrNotReportRepository extends JpaRepository<BuyOrNotReport, BuyOrNotReportId> {

}
