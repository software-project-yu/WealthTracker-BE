package com.WealthTracker.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "올바른 값을 확인해주세요."),
    INVALID_CATEGORY(400,"잘못된 카테고리명입니다."),
    INVALID_VERIFICATION_CODE(400, "유효하지 않은 인증 코드이거나 만료된 코드입니다."),
    EMAIL_VERIFY_NEED(400, "이메일 인증이 필요합니다."),
    EMAIL_ALREADY_REGISTERED(400, "이미 가입된 이메일입니다."),
    EMAIL_VERIFY_FAIL(400, "이메일 인증에 실패했습니다."),
    PASSWORD_RESET_INVALID(400, "비밀번호 재설정 코드가 유효하지 않거나 만료된 코드입니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(404, "존재하지 않는 회원 ID 입니다."),
    VERIFICATION_CODE_NOT_FOUND(404, "인증 코드가 존재하지 않습니다."),
    EXPEND_NOT_FOUND(404, "존재하지 않는 지출 ID입니다."),
    INCOME_NOT_FOUND(404, "존재하지 않는 수입 ID입니다."),


    //409 CONFLICT 중복된 리소스
    USER_NOT_CORRECT(409,"해당 유저의 지출 또는 수입내역이 아닙니다."),
    EMAIL_CONFLICT(409, "해당 이메일은 이미 등록되어 있습니다."),



    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    TOOMANY_REQUEST(500,"API 요청을 잠시 후 다시 시도해 주세요.");




    private final int status;
    private final String message;
}
