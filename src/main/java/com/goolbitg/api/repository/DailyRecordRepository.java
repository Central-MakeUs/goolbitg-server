package com.goolbitg.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goolbitg.api.entity.DailyRecord;
import com.goolbitg.api.entity.DailyRecordId;

/**
 * DailyRecordRepository
 */
public interface DailyRecordRepository extends JpaRepository<DailyRecord, DailyRecordId> {

    List<DailyRecord> findByUserIdAndDateBetween(String userId, LocalDate monday, LocalDate today);

}
