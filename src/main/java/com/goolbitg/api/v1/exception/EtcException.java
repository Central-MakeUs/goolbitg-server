package com.goolbitg.api.v1.exception;

import org.springframework.http.HttpStatus;

public abstract class EtcException {

    public static CommonException imageTypeNotAllowed(String type) {
        return new CommonException(
            7001,
            "허용되지 않는 이미지 타입입니다. (" + type + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}

