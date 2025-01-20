package com.goolbitg.api.controller;

import java.util.Optional;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.goolbitg.api.BuyOrNotApi;

/**
 * BuyOrNotController
 */
@RestController
public class BuyOrNotController implements BuyOrNotApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ControllerUtils.getRequest();
    }

}
