package com.goolbitg.api.v1.repository.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserStatCustomMapper {

    Integer getRankOfSpendingType(
        @Param("userId") String userId,
        @Param("spendingTypeId") Long spendingTypeId
    );

    Integer getTotalCountOfSpendingType(
        @Param("spendingTypeId") Long spendingTypeId
    );
}
