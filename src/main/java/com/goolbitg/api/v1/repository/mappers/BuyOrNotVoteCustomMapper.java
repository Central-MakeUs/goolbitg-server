package com.goolbitg.api.v1.repository.mappers;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.goolbitg.api.v1.entity.custom.BuyOrNotVoteAggregationCustom;

@Mapper
public interface BuyOrNotVoteCustomMapper {

   BuyOrNotVoteAggregationCustom aggregateVote(
        @Param("userId") String userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
