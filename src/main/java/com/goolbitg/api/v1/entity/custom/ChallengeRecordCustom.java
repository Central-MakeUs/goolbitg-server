package com.goolbitg.api.v1.entity.custom;

import com.goolbitg.api.model.ChallengeRecordStatus;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeRecordCustom {

    private Category category;
    private ChallengeRecordStatus status;

}
