package com.goolbitg.api.v1.repository;

import static com.goolbitg.api.v1.entity.challengeGroup.QChallengeGroup.challengeGroup;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.goolbitg.api.v1.entity.challengeGroup.ChallengeGroup;
import com.goolbitg.api.v1.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ChallengeGroupCustomRepository {

    private final JPAQueryFactory query;

    public Page<ChallengeGroup> search(String search, String ownerId, Pageable page) {
        JPAQuery<ChallengeGroup> challengeQuery = query.selectFrom(challengeGroup);

        if (!StringUtils.isNullOrBlank(search)) {
            challengeQuery = challengeQuery
                .where(challengeGroup.hashtags.containsIgnoreCase(search)
                    .or(challengeGroup.title.containsIgnoreCase(search)));
        }

        if (!StringUtils.isNullOrBlank(ownerId)) {
            challengeQuery = challengeQuery
                .where(challengeGroup.ownerId.eq(ownerId));
        }

        // Apply pagination
        List<ChallengeGroup> results = challengeQuery
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .fetch();

        // Get total count for pagination
        long total = query.select(challengeGroup.count())
            .from(challengeGroup)
            .fetchOne();

        return new PageImpl<>(results, page, total);
    }
}
