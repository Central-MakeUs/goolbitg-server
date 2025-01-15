package com.goolbitg.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class NoticeException {

    public static CommonException noticeNotExist(String id) {
        return new CommonException(
            6001,
            "알림이 존재하지 않습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException readAlready(String id) {
        return new CommonException(
            6002,
            "이미 읽은 알림입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
