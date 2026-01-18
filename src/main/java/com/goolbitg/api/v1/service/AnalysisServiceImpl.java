package com.goolbitg.api.v1.service;

import static com.goolbitg.api.model.ChallengeRecordStatus.SUCCESS;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.goolbitg.api.model.AnalysisReportDtoBuyOrNotAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoIndvGroupAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoSummary;
import com.goolbitg.api.v1.repository.ChallengeGroupRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.util.DateUtils;
import com.goolbitg.api.v1.util.DateUtils.DateRange;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final ChallengeRecordRepository recordRepository;
    private final ChallengeGroupRecordRepository groupRecordRepository;

    @Override
    public AnalysisReportDtoSummary getSummary(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSummary'");
    }

    @Override
    public AnalysisReportDtoCompletionAnalysis getCompletionAnalysis(String userId) {
        DateRange prevWeek = DateUtils.getWeekRangeOfDate(LocalDate.now().minusDays(7));
        DateRange thisWeek = DateUtils.getWeekRangeOfDate(LocalDate.now());

        int prevRC = recordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, prevWeek.startDate(), prevWeek.endDate());
        int prevGRC = groupRecordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, prevWeek.startDate(), prevWeek.endDate());
        int thisRC = recordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, thisWeek.startDate(), thisWeek.endDate());
        int thisGRC = groupRecordRepository.countByUserAndStatusAndDateRange(userId, SUCCESS, thisWeek.startDate(), thisWeek.endDate());

        int prevTotal = prevRC + prevGRC;
        int thisTotal = thisRC + thisGRC;
        var response = new AnalysisReportDtoCompletionAnalysis();
        response.setMessage(String.format("이번주에 %d개의 챌린지를 완료했어요!", thisTotal));
        response.setPrev(prevTotal);
        response.setCurrent(thisTotal);
        response.setRecommandation(thisTotal + 2);

        return response;
    }

    @Override
    public AnalysisReportDtoCategoryAnalysis getCategoryAnalysis(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryAnalysis'");
    }

    @Override
    public AnalysisReportDtoIndvGroupAnalysis getIndvGroupAnalysis(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIndvGroupAnalysis'");
    }

    @Override
    public AnalysisReportDtoBuyOrNotAnalysis getBuyOrNotAnalysis(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBuyOrNotAnalysis'");
    }
}
