package com.goolbitg.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AuthException {

    public static CommonException tokenNotValid(String token) {
        return new CommonException(
            2001,
            "토큰이 유효하지 않습니다. (" + token + ")",
            HttpStatus.UNAUTHORIZED);
    }
    public static CommonException tokenExpired(String token) {
        return new CommonException(
            2002,
            "토큰이 만료되었습니다. (" + token + ")",
            HttpStatus.UNAUTHORIZED);
    }
    public static CommonException tokenNotExist() {
        return new CommonException(
            2003,
            "인증 정보가 없습니다.",
            HttpStatus.UNAUTHORIZED);
    }
    public static CommonException notAllowed() {
        return new CommonException(
            2004,
            "접근 권한이 없습니다.",
            HttpStatus.FORBIDDEN);
    }

}
