package com.goolbitg.api.service;

import static com.goolbitg.api.model.ChallengeRecordStatus.FAIL;
import static com.goolbitg.api.model.ChallengeRecordStatus.SUCCESS;
import static com.goolbitg.api.model.ChallengeRecordStatus.WAIT;
import static com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category.FOOD;
import static com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category.LIVING;
import static com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category.SHOPING;
import static com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category.TRAFFIC;
import static com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category.ETC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goolbitg.api.model.AnalysisReportDtoBuyOrNotAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoIndvGroupAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoSummary;
import com.goolbitg.api.model.SpendingTypeDto;
import com.goolbitg.api.model.UserDto;
import com.goolbitg.api.v1.entity.custom.BuyOrNotVoteAggregationCustom;
import com.goolbitg.api.v1.entity.custom.ChallengeRecordAggregationCustom;
import com.goolbitg.api.v1.entity.custom.ChallengeRecordCustom;
import com.goolbitg.api.v1.entity.user.SpendingType;
import com.goolbitg.api.v1.entity.user.User;
import com.goolbitg.api.v1.repository.ChallengeGroupRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.repository.UserRepository;
import com.goolbitg.api.v1.repository.mappers.BuyOrNotVoteCustomMapper;
import com.goolbitg.api.v1.repository.mappers.ChallengeRecordCustomMapper;
import com.goolbitg.api.v1.repository.mappers.UserStatCustomMapper;
import com.goolbitg.api.v1.service.AnalysisService;
import com.goolbitg.api.v1.service.AnalysisServiceImpl;
import com.goolbitg.api.v1.service.UserService;

@ExtendWith(MockitoExtension.class)
public class AnalysisServiceMockTest {

    AnalysisService sut;
    @Mock ChallengeRecordRepository recordRepository;
    @Mock ChallengeGroupRecordRepository groupRecordRepository;
    @Mock ChallengeRecordCustomMapper recordCustomMapper;
    @Mock BuyOrNotVoteCustomMapper buyOrNotVoteCustomMapper;
    @Mock UserStatCustomMapper userStatCustomMapper;
    @Mock UserService userService;

    @BeforeEach
    void setup() {
        sut = new AnalysisServiceImpl(
            recordRepository,
            groupRecordRepository,
            recordCustomMapper,
            buyOrNotVoteCustomMapper,
            userStatCustomMapper,
            userService
        );
    }

    @Test
    void getCompletionAnalysis_S() {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 14);
        final LocalDate startOfThisWeek = LocalDate.of(2026, 1, 12);
        final LocalDate endOfThisWeek = LocalDate.of(2026, 1, 19);
        final LocalDate startOfPrevWeek = LocalDate.of(2026, 1, 5);
        final LocalDate endOfPrevWeek = LocalDate.of(2026, 1, 12);
        final String userId = "test_id";
        final int thisRC = 3;
        final int thisGRC = 5;
        final int prevRC = 2;
        final int prevGRC = 1;

        when(recordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, startOfThisWeek, endOfThisWeek))
            .thenReturn(thisRC);
        when(groupRecordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, startOfThisWeek, endOfThisWeek))
            .thenReturn(thisGRC);
        when(recordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, startOfPrevWeek, endOfPrevWeek))
            .thenReturn(prevRC);
        when(groupRecordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, startOfPrevWeek, endOfPrevWeek))
            .thenReturn(prevGRC);

        // when
        AnalysisReportDtoCompletionAnalysis analysis = sut.getCompletionAnalysis(userId, today);

        // then
        verify(recordRepository).countByUserAndStatusAndDateRange(userId, SUCCESS, startOfThisWeek, endOfThisWeek);
        verify(groupRecordRepository).countByUserAndStatusAndDateRange(userId, SUCCESS, startOfThisWeek, endOfThisWeek);
        verify(recordRepository).countByUserAndStatusAndDateRange(userId, SUCCESS, startOfPrevWeek, endOfPrevWeek);
        verify(groupRecordRepository).countByUserAndStatusAndDateRange(userId, SUCCESS, startOfPrevWeek, endOfPrevWeek);

        final int prevTotal = prevRC + prevGRC;
        final int thisTotal = thisRC + thisGRC;
        assertThat(analysis.getMessage()).contains(String.format("이번주에 %d개의 챌린지를 완료했어요!", thisTotal));
        assertThat(analysis.getPrev()).isEqualTo(prevTotal);
        assertThat(analysis.getCurrent()).isEqualTo(thisTotal);
        assertThat(analysis.getRecommandation()).isEqualTo(thisTotal + 2);
    }

    @Test
    void getCategoryAnalysis_S_normal() {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 14);
        final LocalDate startOfWeek = LocalDate.of(2026, 1, 12);
        final LocalDate endOfWeek = LocalDate.of(2026, 1, 19);
        final String userId = "test_id";
        final List<ChallengeRecordCustom> records = List.of(
            new ChallengeRecordCustom(FOOD, SUCCESS),
            new ChallengeRecordCustom(FOOD, SUCCESS),
            new ChallengeRecordCustom(FOOD, FAIL),
            new ChallengeRecordCustom(TRAFFIC, WAIT),
            new ChallengeRecordCustom(SHOPING, FAIL),
            new ChallengeRecordCustom(LIVING, SUCCESS)
        );

        when(recordCustomMapper.findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek))
            .thenReturn(records);

        // when
        AnalysisReportDtoCategoryAnalysis analysis = sut.getCategoryAnalysis(userId, today);

        // then
        verify(recordCustomMapper).findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek);
        assertThat(analysis).isNotNull();
        assertThat(analysis.getMessage()).contains("1개의 카테고리를 모두 성공했어요!");
        assertThat(analysis.getScores()).hasSize(5);
        assertThat(analysis.getScores().get(0).getCatName()).isEqualTo(FOOD.getKoName());
        assertThat(analysis.getScores().get(0).getTotal()).isEqualTo(3);
        assertThat(analysis.getScores().get(0).getSuccess()).isEqualTo(2);
        assertThat(analysis.getScores().get(1).getCatName()).isEqualTo(TRAFFIC.getKoName());
        assertThat(analysis.getScores().get(1).getTotal()).isEqualTo(1);
        assertThat(analysis.getScores().get(1).getSuccess()).isEqualTo(0);
        assertThat(analysis.getScores().get(2).getCatName()).isEqualTo(SHOPING.getKoName());
        assertThat(analysis.getScores().get(2).getTotal()).isEqualTo(1);
        assertThat(analysis.getScores().get(2).getSuccess()).isEqualTo(0);
        assertThat(analysis.getScores().get(3).getCatName()).isEqualTo(LIVING.getKoName());
        assertThat(analysis.getScores().get(3).getTotal()).isEqualTo(1);
        assertThat(analysis.getScores().get(3).getSuccess()).isEqualTo(1);
        assertThat(analysis.getScores().get(4).getCatName()).isEqualTo(ETC.getKoName());
        assertThat(analysis.getScores().get(4).getTotal()).isEqualTo(0);
        assertThat(analysis.getScores().get(4).getSuccess()).isEqualTo(0);
    }


    @Test
    void getCategoryAnalysis_S_no_success() {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 14);
        final LocalDate startOfWeek = LocalDate.of(2026, 1, 12);
        final LocalDate endOfWeek = LocalDate.of(2026, 1, 19);
        final String userId = "test_id";
        final List<ChallengeRecordCustom> records = List.of(
            new ChallengeRecordCustom(FOOD, FAIL),
            new ChallengeRecordCustom(TRAFFIC, WAIT)
        );

        when(recordCustomMapper.findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek))
            .thenReturn(records);

        // when
        AnalysisReportDtoCategoryAnalysis analysis = sut.getCategoryAnalysis(userId, today);

        // then
        verify(recordCustomMapper).findByUserIdAndDateBetween(userId, startOfWeek, endOfWeek);
        assertThat(analysis).isNotNull();
        assertThat(analysis.getMessage()).contains("성공한 카테고리가 없어요!");
        assertThat(analysis.getScores()).hasSize(5);
        assertThat(analysis.getScores().get(0).getCatName()).isEqualTo(FOOD.getKoName());
        assertThat(analysis.getScores().get(0).getTotal()).isEqualTo(1);
        assertThat(analysis.getScores().get(0).getSuccess()).isEqualTo(0);
        assertThat(analysis.getScores().get(1).getCatName()).isEqualTo(TRAFFIC.getKoName());
        assertThat(analysis.getScores().get(1).getTotal()).isEqualTo(1);
        assertThat(analysis.getScores().get(1).getSuccess()).isEqualTo(0);
        assertThat(analysis.getScores().get(2).getCatName()).isEqualTo(SHOPING.getKoName());
        assertThat(analysis.getScores().get(2).getTotal()).isEqualTo(0);
        assertThat(analysis.getScores().get(2).getSuccess()).isEqualTo(0);
        assertThat(analysis.getScores().get(3).getCatName()).isEqualTo(LIVING.getKoName());
        assertThat(analysis.getScores().get(3).getTotal()).isEqualTo(0);
        assertThat(analysis.getScores().get(3).getSuccess()).isEqualTo(0);
        assertThat(analysis.getScores().get(4).getCatName()).isEqualTo(ETC.getKoName());
        assertThat(analysis.getScores().get(4).getTotal()).isEqualTo(0);
        assertThat(analysis.getScores().get(4).getSuccess()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({
        "4, 2, 5, 5, 0.5 , 1.0, 함께할 때 성공률이 50% 높아요!",
        "4, 3, 2, 0, 0.75, 0.0, 혼자할 때 성공률이 75% 높아요!",
        "0, 0, 0, 0, 0.0 , 0.0, 참여한 챌린지가 없어요!"       ,
        "1, 0, 1, 0, 0.0 , 0.0, 성공한 챌린지가 없어요!"       ,
        "1, 1, 1, 1, 1.0 , 1.0, 성공률이 반반이에요!"          ,
    })
    void getIndvGroupAnalysis_S(
        int indvTotal,
        int indvSuccess,
        int groupTotal,
        int groupSuccess,
        float indvScore,
        float groupScore,
        String message
    ) {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 14);
        final LocalDate startOfWeek = LocalDate.of(2026, 1, 12);
        final LocalDate endOfWeek = LocalDate.of(2026, 1, 19);
        final String userId = "test_id";
        final ChallengeRecordAggregationCustom aggregation = new ChallengeRecordAggregationCustom();
        aggregation.setIndvTotal(indvTotal);
        aggregation.setIndvSuccess(indvSuccess);
        aggregation.setGroupTotal(groupTotal);
        aggregation.setGroupSuccess(groupSuccess);

        when(recordCustomMapper.aggregateByUserIdAndDateBetween(userId, startOfWeek, endOfWeek))
            .thenReturn(aggregation);

        // when
        AnalysisReportDtoIndvGroupAnalysis analysis = sut.getIndvGroupAnalysis(userId, today);

        // then
        verify(recordCustomMapper).aggregateByUserIdAndDateBetween(userId, startOfWeek, endOfWeek);
        assertThat(analysis).isNotNull();
        assertThat(analysis.getMessage()).contains(message);
        assertThat(analysis.getIndvScore()).isEqualTo(indvScore);
        assertThat(analysis.getGroupScore()).isEqualTo(groupScore);
    }

    @ParameterizedTest
    @CsvSource({
        "3, 1, 살까가 50% 더 높아요!",
        "0, 2, 말까가 100% 더 높아요!",
        "2, 2, 의견이 반반이에요!",
        "0, 0, 투표결과가 없어요!",
    })
    void getBuyOrNotAnalysis_S(int goodCount, int badCount, String message) {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 14);
        final LocalDate startOfWeek = LocalDate.of(2026, 1, 12);
        final LocalDate endOfWeek = LocalDate.of(2026, 1, 19);
        final String userId = "test_id";
        final BuyOrNotVoteAggregationCustom aggregation = new BuyOrNotVoteAggregationCustom();
        aggregation.setGoodCount(goodCount);
        aggregation.setBadCount(badCount);

        when(buyOrNotVoteCustomMapper.aggregateVote(userId, startOfWeek, endOfWeek))
            .thenReturn(aggregation);

        // when
        AnalysisReportDtoBuyOrNotAnalysis analysis = sut.getBuyOrNotAnalysis(userId, today);

        // then
        verify(buyOrNotVoteCustomMapper).aggregateVote(userId, startOfWeek, endOfWeek);
        assertThat(analysis.getBuyScore()).isEqualTo(goodCount);
        assertThat(analysis.getNotScore()).isEqualTo(badCount);
        assertThat(analysis.getMessage()).isEqualTo(message);
    }

    @Test
    void getSummary_S() {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 14);
        final String userId = "test_id";
        final Long spendingTypeId = 1L;
        final SpendingTypeDto spendingType = new SpendingTypeDto();
        spendingType.setId(spendingTypeId);
        spendingType.setImageUrl(URI.create("http://testimageurl.com/1"));
        spendingType.setTitle("test type");
        final UserDto user = new UserDto();
        user.setId(userId);
        user.setNickname("testnickname");
        user.setSpendingType(spendingType);

        when(userService.getUser(userId)).thenReturn(user);
        when(userStatCustomMapper.getTotalCountOfSpendingType(spendingTypeId))
            .thenReturn(30);
        when(userStatCustomMapper.getRankOfSpendingType(userId, spendingTypeId))
            .thenReturn(3);

        // when
        AnalysisReportDtoSummary summary = sut.getSummary(userId, today);

        // then
        verify(userService).getUser(userId);
        verify(userStatCustomMapper).getTotalCountOfSpendingType(spendingTypeId);
        verify(userStatCustomMapper).getRankOfSpendingType(userId, spendingTypeId);
        assertThat(summary).isNotNull();
        assertThat(summary.getUsername()).isEqualTo(user.getNickname());
        assertThat(summary.getPercantage()).isEqualTo(10);
        assertThat(summary.getImageUrl()).isEqualTo(spendingType.getImageUrl().toString());
        assertThat(summary.getSpendingType()).isEqualTo(spendingType.getTitle());
    }
}
