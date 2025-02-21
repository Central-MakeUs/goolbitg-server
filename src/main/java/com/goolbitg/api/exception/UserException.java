package com.goolbitg.api.exception;

import org.springframework.http.HttpStatus;

public abstract class UserException {

    public static CommonException alreadyRegistered(String id) {
        return new CommonException(
            3001,
            "이미 등록된 회원입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException userNotExist(String id) {
        return new CommonException(
            3002,
            "등록되지 않은 회원입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException registrationNotComplete(String id) {
        return new CommonException(
            3003,
            "회원정보 입력이 완료되지 않았습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException previousStepNotComplete(int status) {
        return new CommonException(
            3004,
            "이전 단계의 정보 입력이 완료되지 않았습니다. (" + status + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException savingIsGreaterThanIncome() {
        return new CommonException(
            3005,
            "평균 저축은 평균 수입보다 높을 수 없습니다.",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
