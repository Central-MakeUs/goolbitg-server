package com.goolbitg.api.service;

import static com.goolbitg.api.model.ChallengeRecordStatus.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.v1.repository.ChallengeGroupRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.service.AnalysisService;
import com.goolbitg.api.v1.service.AnalysisServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AnalysisServiceMockTest {

    AnalysisService sut;
    @Mock ChallengeRecordRepository recordRepository;
    @Mock ChallengeGroupRecordRepository groupRecordRepository;

    @BeforeEach
    void setup() {
        sut = new AnalysisServiceImpl(
            recordRepository,
            groupRecordRepository
        );
    }

    @Test
    void getCompletionAnalysis_S() {
        // given
        final LocalDate today = LocalDate.of(2026, 1, 15);
        final LocalDate startOfThisWeek = LocalDate.of(2026, 1, 12);
        final LocalDate endOfThisWeek = LocalDate.of(2026, 1, 18);
        final LocalDate startOfPrevWeek = LocalDate.of(2026, 1, 5);
        final LocalDate endOfPrevWeek = LocalDate.of(2026, 1, 11);
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
        AnalysisReportDtoCompletionAnalysis analysis = sut.getCompletionAnalysis(userId);

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
}
