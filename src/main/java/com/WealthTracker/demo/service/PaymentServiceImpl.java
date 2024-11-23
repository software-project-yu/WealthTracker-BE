package com.WealthTracker.demo.service;


import com.WealthTracker.demo.DTO.PaymentRequestDTO;
import com.WealthTracker.demo.DTO.PaymentResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.Payment;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.Asset;
import com.WealthTracker.demo.enums.PaymentDetail;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.PaymentRepository;
import com.WealthTracker.demo.repository.UserRepository;

import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor

public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public Long writePayment(PaymentRequestDTO paymentRequestDTO, String token) {
        Long userId = jwtUtil.getUserId(token);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (paymentRequestDTO.getPaymentDetail() == null || paymentRequestDTO.getPaymentDetail().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_DETAIL1);
        }

        PaymentDetail convertToPaymentDetail;
        try {
            convertToPaymentDetail = PaymentDetail.fromString(paymentRequestDTO.getPaymentDetail());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_DETAIL2);
        }

        if (paymentRequestDTO.getDueDate() == null || paymentRequestDTO.getLastPayment() == null) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_DETAIL3);
        }

        LocalDate dueDate;
        LocalDate lastPayment;
        try {
            dueDate = LocalDate.parse(paymentRequestDTO.getDueDate());
            lastPayment = LocalDate.parse(paymentRequestDTO.getLastPayment());
        } catch (DateTimeParseException e) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_DETAIL4);
        }

        Payment payment = Payment.builder()
                .dueDate(dueDate.atStartOfDay())
                .paymentDetail(convertToPaymentDetail)
                .lastPayment(lastPayment.atStartOfDay())
                .cost(paymentRequestDTO.getCost())
                .tradeName(paymentRequestDTO.getTradeName())
                .user(user)
                .build();

        try {
            return paymentRepository.save(payment).getPaymentId();
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_DETAIL5);
        }
    }
    @Override
    public List<PaymentResponseDTO> listPayments(String token) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Payment> paymentList = paymentRepository.findAllByUser(user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        ));
        if (paymentList.isEmpty()) {
            // 결제 내역이 없을 경우 예외 발생
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        return paymentList.stream()
                .map(payment -> PaymentResponseDTO.builder()
                        .paymentId(payment.getPaymentId())
                        .dueDate(payment.getDueDate().toString().substring(0, 10))
                        .paymentDetail(PaymentDetail.toString(payment.getPaymentDetail()))
                        .lastPayment(payment.getLastPayment().toString().substring(0, 10))
                        .cost(payment.getCost())
                        .tradeName(payment.getTradeName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Long updatePayment(String token, Long paymentId, PaymentRequestDTO paymentRequestDTO) {
        // 유저 정보 가져오기
        User myUser = userRepository.findByUserId(jwtUtil.getUserId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 결제아이디로 결제 내역 찾기
        Payment findPayment = paymentRepository.findByPaymentId(paymentId)
                .orElse(null);
        if (findPayment == null) {
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        // 유저의 지출 내역인지 확인
        if (!Objects.equals(findPayment.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT);
        }
        // 결제 내역 조회
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        // 결제 내역 수정
        Payment updatePayment = findPayment.builder()
                .dueDate(LocalDateTime.parse(paymentRequestDTO.getDueDate() + "T00:00"))
                .paymentDetail(PaymentDetail.fromString(paymentRequestDTO.getPaymentDetail()))
                .lastPayment(LocalDateTime.parse(paymentRequestDTO.getLastPayment() + "T00:00"))
                .cost(paymentRequestDTO.getCost())
                .tradeName(paymentRequestDTO.getTradeName())
                .build();
        // 변경된 결제 내역 저장
        paymentRepository.save(updatePayment);
        // 수정된 결제 내역 ID 반환
        return updatePayment.getPaymentId();
    }

    @Override
    public Long deletePayment(String token, Long paymentId) {
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User currentUser = user.orElseThrow(() ->  new CustomException(ErrorCode.USER_NOT_FOUND));

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        // 결제 내역 삭제
        paymentRepository.delete(payment);
        return paymentId;
    }
}