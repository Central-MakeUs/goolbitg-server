package com.goolbitg.api.exception;

import org.springframework.http.HttpStatus;

public abstract class NoticeException {

    public static CommonException noticeNotExist(Long id) {
        return new CommonException(
            6001,
            "알림이 존재하지 않습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException readAlready(Long id) {
        return new CommonException(
            6002,
            "이미 읽은 알림입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException registrationTokenAlreadyRegistered(String token) {
        return new CommonException(
            6003,
            "이미 등록된 레지스트레이션 토큰입니다. (" + token + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
