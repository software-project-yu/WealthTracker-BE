package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.Payment;
import com.WealthTracker.demo.domain.User;
//import org.jetbrains.annotations.NotNull;
import com.WealthTracker.demo.enums.Category_Expend;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 사용자의 결제 내역 조회
    List<Payment> findAllByUser(User user);
    // 결제 ID로 결제 내역 조회
    Optional<Payment> findByPaymentId(Long paymentId);

    // 사용자의 결제 금액 합산
    @Query("SELECT SUM(p.cost) FROM Payment p WHERE p.user.userId = :userId")
    Long sumByUserId(@Param("userId") Long userId);
    // 결제 예정일이 된 결제 내역 가져오는 메서드
    List<Payment> findByDueDate(LocalDateTime dueDate);

    //날짜로 최신순 정렬하여 2개 가져오기
    @Query("select e from Payment e order by e.lastPayment desc")
    Optional<List<Payment>> findRecentPayment(Pageable pageable);

    //이번 달 최신 결제 내역 2개
    @Query("SELECT p FROM Payment p WHERE p.user = :user "+
            "and month (p.lastPayment) = month (current_date) "+
            "and year (p.lastPayment) = year (current_date) "+
            " ORDER BY p.lastPayment DESC")
    Optional<List<Payment>> findRecentPayment(User user, Pageable pageable);

    // 이번 달 결제 금액
    @Query("SELECT COALESCE(SUM(p.cost), 0) FROM Payment p "+
            "where p.user =:user "+
            "and month (p.dueDate) = month (current_date) "+
            "and year (p.dueDate) = year (current_date)")
    Long thisMonthAmount(@Param("user") User user);

    // 저번 달 결제 금액
    @Query("SELECT COALESCE(SUM(p.cost), 0) FROM Payment p " +
            "where p.user =:user "+
            "and ((month(p.dueDate) = month(current_date ) -1 " +
            "and year(p.dueDate) = year (current_date)) " +
            "or (month(current_date) = 1 and month(p.dueDate) = 12 " +
            "and year(p.dueDate) = year(current_date) -1))"
    )
    Long preMonthAmount(@Param("user") User user);


    void deleteById(@Param("paymentId") Long paymentId);
}