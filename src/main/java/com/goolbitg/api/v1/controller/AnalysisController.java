package com.goolbitg.api.v1.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.AnalysisApi;
import com.goolbitg.api.model.AnalysisReportDto;

@RestController
public class AnalysisController implements AnalysisApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }


    @Override
    public ResponseEntity<AnalysisReportDto> getAnalysisReport() throws Exception {
        // TODO Auto-generated method stub
        return AnalysisApi.super.getAnalysisReport();
    }

    
}
