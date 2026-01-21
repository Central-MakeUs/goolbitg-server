package com.goolbitg.api.v1.entity.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRecordAggregationCustom {


    private Integer indvTotal;
    private Integer indvSuccess;
    private Integer groupTotal;
    private Integer groupSuccess;

}
