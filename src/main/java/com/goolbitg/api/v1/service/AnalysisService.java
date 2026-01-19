package com.goolbitg.api.v1.service;

import java.time.LocalDate;

import com.goolbitg.api.model.AnalysisReportDtoBuyOrNotAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoIndvGroupAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoSummary;

public interface AnalysisService {

    AnalysisReportDtoSummary getSummary(String userId, LocalDate date);
    AnalysisReportDtoCompletionAnalysis getCompletionAnalysis(String userId, LocalDate date);
    AnalysisReportDtoCategoryAnalysis getCategoryAnalysis(String userId, LocalDate date);
    AnalysisReportDtoIndvGroupAnalysis getIndvGroupAnalysis(String userId, LocalDate date);
    AnalysisReportDtoBuyOrNotAnalysis getBuyOrNotAnalysis(String userId, LocalDate date);
}
