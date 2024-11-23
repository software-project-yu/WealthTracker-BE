package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.PaymentRequestDTO;
import com.WealthTracker.demo.DTO.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {
    // 결제 내역 쓰기
    Long writePayment(PaymentRequestDTO paymentRequestDTO, String token);
    // 결제 내역 모두 보기
    List<PaymentResponseDTO> listPayments(String token);
    // 결제 내역 수정
    Long updatePayment(String token, Long paymentId, PaymentRequestDTO paymentRequestDTO);
    // 결제 내역 삭제
    Long deletePayment(String token,Long paymentId);
}
