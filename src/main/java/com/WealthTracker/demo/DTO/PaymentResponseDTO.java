package com.WealthTracker.demo.DTO;

import com.WealthTracker.demo.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class PaymentResponseDTO {
    //  결제 ID
    private Long paymentId;
    // 결제 상세 내용
    private String paymentDetail;
    // 결제 예정일
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime dueDate;
    // 마지막 결제일
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime lastPayment;
    // 결제 금액
    private Long cost;
    // 상호명
    private String tradeName;

    public PaymentResponseDTO(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.paymentDetail = String.valueOf(payment.getPaymentDetail());
        this.dueDate = payment.getDueDate();
        this.lastPayment = payment.getLastPayment();
        this.cost = payment.getCost();
        this.tradeName = payment.getTradeName();
    }
}