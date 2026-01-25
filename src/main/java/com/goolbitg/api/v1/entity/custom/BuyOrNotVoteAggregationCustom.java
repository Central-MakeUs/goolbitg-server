package com.goolbitg.api.v1.entity.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyOrNotVoteAggregationCustom {

    private Integer goodCount;
    private Integer badCount;

}
