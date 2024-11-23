package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Payment;
import com.WealthTracker.demo.domain.User;
//import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 사용자의 결제 내역 조회
    List<Payment> findAllByUser(User user);
    // 결제 ID로 결제 내역 조회 findById
    Optional<Payment> findByPaymentId(Long paymentId);
}