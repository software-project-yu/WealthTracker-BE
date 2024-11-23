package com.WealthTracker.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequestDTO {
    //  결제 ID
    private Long paymentId;
    // 결제 상세 내용
    private String paymentDetail;
    // 결제 예정일
    private String dueDate;
    // 마지막 결제일
    private String lastPayment;
    // 결제 금액
    private Long cost;
    // 상호명
    private String tradeName;
}
