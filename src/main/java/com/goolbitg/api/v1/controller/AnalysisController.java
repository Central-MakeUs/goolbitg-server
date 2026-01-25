package com.goolbitg.api.v1.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.AnalysisApi;
import com.goolbitg.api.model.AnalysisReportDto;
import com.goolbitg.api.model.AnalysisReportDtoBuyOrNotAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCategoryAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoCompletionAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoIndvGroupAnalysis;
import com.goolbitg.api.model.AnalysisReportDtoSummary;
import com.goolbitg.api.v1.security.AuthUtil;
import com.goolbitg.api.v1.service.AnalysisService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnalysisController implements AnalysisApi {

    private final AnalysisService analysisService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }


    @Override
    public ResponseEntity<AnalysisReportDto> getAnalysisReport() throws Exception {
        var date = LocalDate.now();
        String userId = AuthUtil.getLoginUserId();
        AnalysisReportDtoSummary summary = analysisService.getSummary(userId, date);
        AnalysisReportDtoCompletionAnalysis completionAnalysis = analysisService.getCompletionAnalysis(userId, date);
        AnalysisReportDtoCategoryAnalysis categoryAnalysis = analysisService.getCategoryAnalysis(userId, date);
        AnalysisReportDtoIndvGroupAnalysis indvGroupAnalysis = analysisService.getIndvGroupAnalysis(userId, date);
        AnalysisReportDtoBuyOrNotAnalysis buyOrNotAnalysis = analysisService.getBuyOrNotAnalysis(userId, date);

        AnalysisReportDto result = new AnalysisReportDto();
        result.setSummary(summary);
        result.setCompletionAnalysis(completionAnalysis);
        result.setCategoryAnalysis(categoryAnalysis);
        result.setIndvGroupAnalysis(indvGroupAnalysis);
        result.setBuyOrNotAnalysis(buyOrNotAnalysis);
        return ResponseEntity.of(Optional.of(result));
    }

    
}
