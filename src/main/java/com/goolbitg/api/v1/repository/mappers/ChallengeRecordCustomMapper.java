package com.goolbitg.api.v1.repository.mappers;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.goolbitg.api.v1.entity.custom.ChallengeRecordAggregationCustom;
import com.goolbitg.api.v1.entity.custom.ChallengeRecordCustom;

@Mapper
public interface ChallengeRecordCustomMapper {

    List<ChallengeRecordCustom> findByUserIdAndDateBetween(
        @Param("userId") String userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    ChallengeRecordAggregationCustom aggregateByUserIdAndDateBetween(
        @Param("userId") String userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

}
