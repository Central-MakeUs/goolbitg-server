package com.goolbitg.api.v1.service;

import com.goolbitg.api.model.AnalysisReportDtoBuyOrNotAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoIndvGroupAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoSummary;

public interface AnalysisService {

    AnalysisReportDtoSummary getSummary(String userId);
    AnalysisReportDtoCompletionAnalysis getCompletionAnalysis(String userId);
    AnalysisReportDtoCategoryAnalysis getCategoryAnalysis(String userId);
    AnalysisReportDtoIndvGroupAnalysis getIndvGroupAnalysis(String userId);
    AnalysisReportDtoBuyOrNotAnalysis getBuyOrNotAnalysis(String userId);
}
