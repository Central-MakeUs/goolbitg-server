package com.goolbitg.api.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.EtcApi;

/**
 * ImageController
 */
@RestController
public class EtcController implements EtcApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
