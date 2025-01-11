package com.goolbitg.api.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.ChallengeGroupRecordApi;

/**
 * ChallengeGroupRecordController
 */
@RestController
public class ChallengeGroupRecordController implements ChallengeGroupRecordApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
