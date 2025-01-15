package com.goolbitg.api.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.EtcApi;
import com.goolbitg.api.exceptions.AuthException;

/**
 * ImageController
 */
@RestController
public class EtcController implements EtcApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

    @Override
    public ResponseEntity<Void> checkHealth() throws Exception {
        // NOTE: for test
        throw AuthException.tokenNotExist();
    }

}
