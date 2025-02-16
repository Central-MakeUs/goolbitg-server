package com.goolbitg.api.exception;

import org.springframework.http.HttpStatus;

public abstract class BuyOrNotException {

    public static CommonException postLimitExceed() {
        return new CommonException(
            5001,
            "포스트 등록 한도를 초과하였습니다.",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException postNotExist(Long id) {
        return new CommonException(
            5002,
            "포스트가 존재하지 않습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException postAlreadyVoted(Long id) {
        return new CommonException(
            5003,
            "내가 작성한 포스트는 투표할 수 없습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException postAlreadyReported(Long id) {
        return new CommonException(
            5004,
            "이미 신고한 포스트입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
