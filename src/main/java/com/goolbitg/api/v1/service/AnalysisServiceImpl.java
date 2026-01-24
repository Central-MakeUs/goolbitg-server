package com.goolbitg.api.v1.service;

import static com.goolbitg.api.model.ChallengeRecordStatus.SUCCESS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.goolbitg.api.model.AnalysisReportDtoBuyOrNotAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysisScoresInner;
import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoIndvGroupAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoSummary;
import com.goolbitg.api.v1.entity.challengeGroup.enumeration.Category;
import com.goolbitg.api.v1.entity.custom.ChallengeRecordAggregationCustom;
import com.goolbitg.api.v1.entity.custom.ChallengeRecordCustom;
import com.goolbitg.api.v1.repository.ChallengeGroupRecordRepository;
import com.goolbitg.api.v1.repository.ChallengeRecordRepository;
import com.goolbitg.api.v1.repository.mappers.ChallengeRecordCustomMapper;
import com.goolbitg.api.v1.util.DateUtils;
import com.goolbitg.api.v1.util.DateUtils.DateRange;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final ChallengeRecordRepository recordRepository;
    private final ChallengeGroupRecordRepository groupRecordRepository;
    private final ChallengeRecordCustomMapper recordCustomMapper;

    @Override
    public AnalysisReportDtoSummary getSummary(String userId, LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSummary'");
    }

    @Override
    public AnalysisReportDtoCompletionAnalysis getCompletionAnalysis(String userId, LocalDate date) {
        DateRange prevWeek = DateUtils.getWeekRangeOfDate(date.minusDays(7));
        DateRange thisWeek = DateUtils.getWeekRangeOfDate(date);

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
    public AnalysisReportDtoCategoryAnalysis getCategoryAnalysis(String userId, LocalDate date) {
        DateRange thisWeek = DateUtils.getWeekRangeOfDate(date);
        List<ChallengeRecordCustom> result = recordCustomMapper.findByUserIdAndDateBetween(
            userId,
            thisWeek.startDate(),
            thisWeek.endDate()
        );

        List<AnalysisReportDtoCategoryAnalysisScoresInner> scores = new ArrayList<>();
        int completeCount = 0;
        for (Category cat : Category.values()) {
            var inner = new AnalysisReportDtoCategoryAnalysisScoresInner(); 
            inner.setCatName(cat.getKoName());
            int total = 0, success = 0;
            for (ChallengeRecordCustom item : result) {
                if (cat == item.getCategory()) {
                    total += 1;
                    if (item.getStatus() == SUCCESS) {
                        success += 1;
                    }
                }
            }

            inner.setTotal(total);
            inner.setSuccess(success);

            if (total > 0 && total == success)  {
                completeCount += 1;
            }
            scores.add(inner);
        }

        AnalysisReportDtoCategoryAnalysis analysis = new AnalysisReportDtoCategoryAnalysis();

        analysis.setScores(scores);

        if (completeCount == 0) {
            analysis.setMessage("성공한 카테고리가 없어요!");
        } else {
            analysis.setMessage(String.format("%d개의 카테고리를 모두 성공했어요!", completeCount));
        }

        return analysis;
    }

    @Override
    public AnalysisReportDtoIndvGroupAnalysis getIndvGroupAnalysis(String userId, LocalDate date) {
        DateRange thisWeek = DateUtils.getWeekRangeOfDate(date);
        ChallengeRecordAggregationCustom aggregation = 
            recordCustomMapper.aggregateByUserIdAndDateBetween(
                userId,
                thisWeek.startDate(),
                thisWeek.endDate()
            );
        float indvSuccessRatio;
        float groupSuccessRatio;

        if (aggregation.getIndvTotal() > 0) {
            indvSuccessRatio = (float)aggregation.getIndvSuccess() / aggregation.getIndvTotal();
        } else {
            indvSuccessRatio = 0;
        }

        if (aggregation.getGroupTotal() > 0) {
            groupSuccessRatio = (float)aggregation.getGroupSuccess() / aggregation.getGroupTotal();
        } else {
            groupSuccessRatio = 0;
        }

        int diff = Math.abs((int)(indvSuccessRatio * 100 - groupSuccessRatio * 100));

        var result = new AnalysisReportDtoIndvGroupAnalysis();
        if (aggregation.getIndvTotal() == 0 && aggregation.getGroupTotal() == 0) {
            result.setMessage("참여한 챌린지가 없어요!");
        } else if (aggregation.getIndvSuccess() == 0 && aggregation.getGroupSuccess() == 0) {
            result.setMessage("성공한 챌린지가 없어요!");
        } else if (groupSuccessRatio == indvSuccessRatio) {
            result.setMessage("성공률이 반반이에요!");
        } else if (groupSuccessRatio > indvSuccessRatio) {
            result.setMessage(String.format("함께할 때 성공률이 %d%% 높아요!", diff));
        } else if (groupSuccessRatio < indvSuccessRatio) {
            result.setMessage(String.format("혼자할 때 성공률이 %d%% 높아요!", diff));
        }

        result.setIndvScore(indvSuccessRatio);
        result.setGroupScore(groupSuccessRatio);
        return result;
    }

    @Override
    public AnalysisReportDtoBuyOrNotAnalysis getBuyOrNotAnalysis(String userId, LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBuyOrNotAnalysis'");
    }
}
