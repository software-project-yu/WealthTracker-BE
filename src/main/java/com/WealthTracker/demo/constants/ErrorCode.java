package com.WealthTracker.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "올바른 값을 확인해주세요."),
    INVALID_CATEGORY(400, "잘못된 카테고리명입니다."),
    TARGET_ALREADY_EXISTS(400, "이미 존재하는 목표입니다."),
    TARGET_NOT_FOUND(400, "해당 목표가 존재하지 않습니다."),
    INVALID_VERIFICATION_CODE(400, "유효하지 않은 인증 코드입니다."),
    EXPIRED_VERIFICATION_CODE(400, "인증 코드가 만료되었습니다."),
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_VERIFIED(400, "이메일 인증이 완료되지 않았습니다."),
    EMAIL_ALREADY_SENT(400, "이미 인증 코드가 발송되었습니다. 만료 시간을 기다려주세요."),
    EMAIL_ALREADY_REGISTERED(400, "이미 등록된 이메일입니다."),

    // 404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(404, "존재하지 않는 회원 ID입니다."),
    EXPEND_NOT_FOUND(404, "존재하지 않는 지출 ID입니다."),
    INCOME_NOT_FOUND(404, "존재하지 않는 수입 ID입니다."),

    // 409 CONFLICT 중복된 리소스
    USER_NOT_CORRECT(409, "해당 유저의 지출 또는 수입 내역이 아닙니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    TOOMANY_REQUEST(500, "API 요청을 잠시 후 다시 시도해 주세요.");


    private final int status;
    private final String message;
}
