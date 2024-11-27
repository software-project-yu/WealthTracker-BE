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
    PAYMENT_DETAIL_EMPTY(400, "결제 상세 정보가 비어 있습니다."),
    INVALID_PAYMENT_DETAIL_PARAMETER(400, "결제 상세 정보를 변환할 수 없습니다."),
    PAYMENT_DATE_EMPTY(400, "결제 예정일 또는 마지막 결제일이 비어 있습니다."),
    INVALID_PAYMENT_DATE(400, "결제 날짜 형식이 유효하지 않습니다."),
    INVALID_PAYMENT_PARAMETER(400, "잘못된 결제 정보입니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    USER_NOT_FOUND(404, "존재하지 않는 회원 ID 입니다."),
    EXPEND_NOT_FOUND(404, "존재하지 않는 지출 ID입니다."),
    INCOME_NOT_FOUND(404, "존재하지 않는 수입 ID입니다."),
    PAYMENT_NOT_FOUND(404, "존재하지 않는 결제 내역입니다."),
    USER_NOT_FOUND_FOR_PAYMENT(404, "결제 정보를 찾을 수 없는 사용자입니다."),


    //409 CONFLICT 중복된 리소스
    USER_NOT_CORRECT(409,"해당 유저의 지출 또는 수입내역이 아닙니다."),
    PAYMENT_CONFLICT(409, "결제 내역이 이미 존재합니다."),


    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    TOOMANY_REQUEST(500,"API 요청을 잠시 후 다시 시도해 주세요.");


    private final int status;
    private final String message;
}