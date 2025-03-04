package com.goolbitg.api.v1.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.ChallengeGroupApi;

/**
 * ChallengeGroupController
 */
@RestController
public class ChallengeGroupController implements ChallengeGroupApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
