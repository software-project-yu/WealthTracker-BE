package com.WealthTracker.demo.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PaymentAmountDTO {
    // 총합
    private  Long amount;

    // 퍼센트
    private int percent;

    // up or down
    private String upOrDown;

    // 결제 내역 리스트
    private List<PaymentResponseDTO> paymentList;
}
