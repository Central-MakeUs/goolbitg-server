package com.goolbitg.api.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private int code;
    private HttpStatus status;

    public CommonException(int code, String message, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

}
