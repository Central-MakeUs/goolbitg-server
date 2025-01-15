package com.goolbitg.api.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ChallengeException {

    public static CommonException alreadyEnrolled(String id) {
        return new CommonException(
            4001,
            "이미 해당 챌린지에 참여중입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException notEnrolled(String id) {
        return new CommonException(
            4002,
            "해당 챌린지를 참여하고 있지 않습니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException challengeTitleDuplicated(String id) {
        return new CommonException(
            4003,
            "같은 이름의 챌린지가 이미 존재합니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException challengeNotExist(String id) {
        return new CommonException(
            4004,
            "챌린지가 존재하지 않습니다.  (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }
    public static CommonException alreadyComplete(String id) {
        return new CommonException(
            4005,
            "이미 완료한 챌린지입니다. (" + id + ")",
            HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
