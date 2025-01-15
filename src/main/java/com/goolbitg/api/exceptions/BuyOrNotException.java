package com.goolbitg.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BuyOrNotException {

    public static CommonException postLimitExceed(String id) {
        return new CommonException(
            5001,
            "포스트 등록 한도를 초과하였습니다.",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException postNotExist(String id) {
        return new CommonException(
            5002,
            "포스트가 존재하지 않습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
