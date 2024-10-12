package com.WealthTracker.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "올바른 값을 확인해주세요."),

    //404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(404, "존재하지 않는 회원 ID 입니다."),
    EXPEND_NOT_FOUND(404, "존재하지 않는 지출 ID입니다."),
    INCOME_NOT_FOUND(404, "존재하지 않는 수입 ID입니다."),


    //409 CONFLICT 중복된 리소스


    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!");

    private final int status;
    private final String message;
}
