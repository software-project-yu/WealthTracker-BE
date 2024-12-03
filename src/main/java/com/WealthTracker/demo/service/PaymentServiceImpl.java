package com.WealthTracker.demo.service;

import com.WealthTracker.demo.DTO.PaymentAmountDTO;
import com.WealthTracker.demo.DTO.PaymentRequestDTO;
import com.WealthTracker.demo.DTO.PaymentResponseDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendCategoryAmountDTO;
import com.WealthTracker.demo.DTO.income_expend.ExpendResponseDTO;
import com.WealthTracker.demo.constants.ErrorCode;
import com.WealthTracker.demo.domain.Payment;
import com.WealthTracker.demo.domain.User;
import com.WealthTracker.demo.enums.PaymentDetail;
import com.WealthTracker.demo.error.CustomException;
import com.WealthTracker.demo.repository.PaymentRepository;
import com.WealthTracker.demo.repository.UserRepository;

import com.WealthTracker.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));



        if (paymentRequestDTO.getDueDate() == null || paymentRequestDTO.getLastPayment() == null) {
            throw new CustomException(ErrorCode.PAYMENT_DATE_EMPTY,ErrorCode.PAYMENT_DATE_EMPTY.getMessage());
        }



        Payment payment = Payment.builder()
                .dueDate(LocalDate.parse(paymentRequestDTO.getDueDate()).atStartOfDay())
                .paymentDetail(paymentRequestDTO.getPaymentDetail())
                .lastPayment(LocalDate.parse(paymentRequestDTO.getLastPayment()).atStartOfDay())
                .cost(paymentRequestDTO.getCost())
                .tradeName(paymentRequestDTO.getTradeName())
                .user(user)
                .build();

        // 결제 저장
        Payment savePayment = paymentRepository.save(payment);
        // 사용자의 총 결제 금액
        Long totalExpense = paymentRepository.sumByUserId(userId);
        // 사용자의 지출 업데이트
        userRepository.save(user);
        return savePayment.getPaymentId();
    }

    @Override
    public List<PaymentResponseDTO> listPayments(String token) {
        //유저 정보 가져오기
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        List<Payment> paymentList = paymentRepository.findAllByUser(user.orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage())
        ));
        if (paymentList.isEmpty()) {
            // 결제 내역이 없을 경우 예외 발생
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND,ErrorCode.PAYMENT_NOT_FOUND.getMessage());
        }
        return paymentList.stream()
                .map(payment -> PaymentResponseDTO.builder()
                        .paymentId(payment.getPaymentId())
                        .dueDate(payment.getDueDate().toString().substring(0,10))
                        .paymentDetail(payment.getPaymentDetail())
                        .lastPayment(payment.getLastPayment().toString().substring(0,10))
                        .cost(payment.getCost())
                        .tradeName(payment.getTradeName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long updatePayment(String token, Long paymentId, PaymentRequestDTO paymentRequestDTO) {
        // 유저 정보 가져오기
        User myUser = userRepository.findByUserId(jwtUtil.getUserId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));
        // 결제아이디로 결제 내역 찾기
        Payment findPayment = paymentRepository.findByPaymentId(paymentId)
                .orElse(null);
        if (findPayment == null) {
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND,ErrorCode.PAYMENT_NOT_FOUND.getMessage());
        }
        // 유저의 지출 내역인지 확인
        if (!Objects.equals(findPayment.getUser().getUserId(), myUser.getUserId())) {
            throw new CustomException(ErrorCode.USER_NOT_CORRECT,ErrorCode.USER_NOT_CORRECT.getMessage());
        }
        // 결제 내역 조회
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND,ErrorCode.PAYMENT_NOT_FOUND.getMessage()));
        // 결제 내역 수정
        Payment updatePayment = findPayment.toBuilder()
                .dueDate(LocalDateTime.parse(paymentRequestDTO.getDueDate() + "T00:00"))
                .paymentDetail(paymentRequestDTO.getPaymentDetail())
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
    @Transactional
    public void deletePayment(String token, Long paymentId) {
        Optional<User> user = userRepository.findByUserId(jwtUtil.getUserId(token));
        User currentUser = user.orElseThrow(() ->  new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));

        // 결제 내역 삭제
         paymentRepository.deleteById(paymentId);
    }

    @Override
    public List<PaymentResponseDTO> getRecentPayments(String token) {
        // JWT 토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));

        // 사용자의 최근 결제 내역 2개 조회
        List<Payment> recentPaymentList = paymentRepository.findRecentPayment(user, (Pageable) PageRequest.of(0, 2))
                .orElseThrow(()->new  CustomException(ErrorCode.PAYMENT_IS_NULL,ErrorCode.PAYMENT_IS_NULL.getMessage()));
        return recentPaymentList.stream()
                .map(PaymentResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentAmountDTO> getAmountByMonth(String token) {
        //jwt토큰 검증 실시
        Optional<User> findUser = userRepository.findByUserId(jwtUtil.getUserId(token));
        User user = findUser.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,ErrorCode.USER_NOT_FOUND.getMessage()));
        // 월별 결제 내역 저장할 리스트
        List<PaymentAmountDTO> nowMonthList = new ArrayList<>();
        // 이번 달 결제액 불러오기
        Long thisMonthAmount = paymentRepository.thisMonthAmount(user);
        // 저번 달 결제액 불러오기
        Long preMonthAmount = paymentRepository.preMonthAmount(user);

        //퍼센트 계산
        String upOrDown;
        int percentChange;
        if (preMonthAmount == 0) {
            // 저번 달 지출액이 0일 때
            if (thisMonthAmount > 0) {
                percentChange = 100;
                upOrDown = "UP";
            } else {
                percentChange = 0;
                upOrDown = "SAME";
            }
        } else {
            // 저번 달 지출액이 0이 아닐 때
            double change = (double) (thisMonthAmount - preMonthAmount) / preMonthAmount * 100;
            percentChange = Math.abs((int) change);
            upOrDown = change >= 0 ? "UP" : "DOWN";
        }

        // 결제 내역 2개씩 리스트
        List<Payment> paymentList = paymentRepository.findRecentPayment(user, PageRequest.of(0, 2))
                .orElseThrow(()->new CustomException(ErrorCode.PAYMENT_IS_NULL,ErrorCode.EXPEND_NOT_FOUND.getMessage()));
        List<PaymentResponseDTO> paymentResponseDTOList = paymentList.stream()
                .map(payment -> PaymentResponseDTO.builder()
                        .paymentId(payment.getPaymentId())
                        .tradeName(payment.getTradeName())
                        .lastPayment(payment.getLastPayment().toString().substring(0,10))
                        .cost(payment.getCost())
                        .build())
                .toList();
        // 월별 결제 내역 DTO
        PaymentAmountDTO paymentAmountDTO = PaymentAmountDTO.builder()
                .amount(thisMonthAmount)
                .percent(percentChange)
                .upOrDown(upOrDown)
                .paymentList(paymentResponseDTOList)
                .build();

        nowMonthList.add(paymentAmountDTO);

        return nowMonthList;
    }
}