package com.WealthTracker.demo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "올바른 값을 확인해주세요."),
    INVALID_CATEGORY(400,"잘못된 카테고리명입니다."),
    INVALID_PAYMENT_DETAIL(400, "유효하지 않은 결제 상세 정보입니다."),
    INVALID_PAYMENT_DETAIL_PARAMETER(400, "결제 상세 정보를 변환할 수 없습니다."),
    PAYMENT_DATE_EMPTY(400, "결제 예정일 또는 마지막 결제일이 비어 있습니다."),
    INVALID_PAYMENT_DATE(400, "결제 날짜 형식이 유효하지 않습니다."),
    INVALID_PAYMENT_PARAMETER(400, "잘못된 결제 정보입니다."),
    INVALID_VERIFICATION_CODE(400, "유효하지 않은 인증 코드이거나 만료된 코드입니다."),
    EMAIL_VERIFY_NEED(400, "이메일 인증이 필요합니다."),
    EMAIL_ALREADY_REGISTERED(400, "이미 가입된 이메일입니다."),
    EMAIL_VERIFY_FAIL(400, "이메일 인증에 실패했습니다."),
    PASSWORD_RESET_INVALID(400, "비밀번호 재설정 코드가 유효하지 않거나 만료된 코드입니다."),
    INVALID_UPDATE_REQUEST(400, "수정할 정보가 없거나 기존 값과 동일합니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(404, "존재하지 않는 회원 ID 입니다."),
    VERIFICATION_CODE_NOT_FOUND(404, "인증 코드가 존재하지 않습니다."),
    PAYMENT_NOT_FOUND(404, "존재하지 않는 결제 내역입니다."),
    TARGET_ALREADY_EXISTS(400, "이미 존재하는 목표입니다."),
    TARGET_NOT_FOUND(400, "해당 목표가 존재하지 않습니다."),
    PASSWORD_MISMATCH(400, "비밀번호가 일치하지 않습니다."),
    EXPEND_NOT_FOUND(404, "존재하지 않는 지출 ID입니다."),
    INCOME_NOT_FOUND(404, "존재하지 않는 수입 ID입니다."),

    // 409 CONFLICT 중복된 리소스
    USER_NOT_CORRECT(409, "해당 유저의 지출 또는 수입 내역이 아닙니다."),
    EMAIL_CONFLICT(409, "해당 이메일은 이미 등록되어 있습니다."),



    PAYMENT_CONFLICT(409, "결제 내역이 이미 존재합니다."),


    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    TOOMANY_REQUEST(500, "API 요청을 잠시 후 다시 시도해 주세요.");


    private final int status;
    private final String message;
}
