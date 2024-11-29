package com.WealthTracker.demo.enums;

import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.error.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum PaymentDetail {
    RENT("월세"),
    UTILITIES("공과금"),
    INSURANCE("보험료"),
    SUBSCRIPTION("구독 서비스"),
    LOAN("대출");
    // 결제 상세 내용 설명 한글로 반환
    private final String paymentDetailType;
    // 문자열 통해 해당 결제 내역을 찾을 수 있도록 변환
    public static PaymentDetail fromString(String paymentDetailType) {
        for (PaymentDetail detail : PaymentDetail.values()) {
            if (detail.getPaymentDetailType().equalsIgnoreCase(paymentDetailType)) {
                return detail;
            }
        }
        throw new CustomException(ErrorCode.INVALID_PAYMENT_DETAIL);
    }
    public static String toString(PaymentDetail detail) {
        return detail.getPaymentDetailType();
    }
}