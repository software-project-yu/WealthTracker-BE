package com.WealthTracker.demo.DTO;

import jakarta.validation.constraints.NotNull;
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

public class PaymentRequestDTO {
    //  결제 ID
    @NotNull(message = "Payment ID cannot be null")
    private Long paymentId;
    // 결제 상세 내용
    @NotNull(message = "Payment detail cannot be null")
    private String paymentDetail;
    // 결제 예정일
    @NotNull(message = "Due date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime dueDate;
    // 마지막 결제일
    @NotNull(message = "Last payment date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime lastPayment;
    // 결제 금액
    @NotNull(message = "Cost cannot be null")
    private Long cost;
    // 상호명
    @NotNull(message = "Trade name cannot be null")
    private String tradeName;
}