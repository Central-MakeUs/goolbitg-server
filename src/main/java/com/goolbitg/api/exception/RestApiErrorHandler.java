package com.goolbitg.api.exception;

import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.goolbitg.api.model.ErrorDto;

@ControllerAdvice
public class RestApiErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(
        HttpServletRequest request,
        Exception ex,
        Locale locale
    ) {
        ErrorDto error = new ErrorDto();
        error.setCode(1000);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleValidationException(
        HttpServletRequest request,
        IllegalArgumentException ex,
        Locale locale
    ) {
        ErrorDto error = new ErrorDto();
        error.setCode(1001);
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorDto> handleCommonException(
        HttpServletRequest request,
        CommonException ex,
        Locale locale
    ) {
        ErrorDto error = new ErrorDto();
        error.setCode(ex.getCode());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, ex.getStatus());
    }

}
