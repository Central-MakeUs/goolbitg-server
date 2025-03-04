package com.goolbitg.api.v1.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.v1.entity.DailyRecord;
import com.goolbitg.api.v1.entity.DailyRecordId;

/**
 * DailyRecordRepository
 */
public interface DailyRecordRepository extends JpaRepository<DailyRecord, DailyRecordId> {

    List<DailyRecord> findByUserIdAndDateBetween(String userId, LocalDate monday, LocalDate today);

}
