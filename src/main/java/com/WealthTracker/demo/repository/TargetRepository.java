package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Target;
import com.WealthTracker.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Optional<Target> findByTargetIdAndUserUserId(Long targetId, Long userId);

    //월별 목표 금액 리턴 -> 예외 발생하기는 하지만 목표 시작날짜와 끝나는 날짜 모두 같은 월에 해당하먄 리탄
    @Query("select coalesce(sum(t.targetAmount),0) from Target t "+
            "where :month = month(t.startDate) "+
            "and :month = month(t.endDate) "+
            "and t.user = :user")
    int getAllTargetAmountByMonth(@Param("month")int month,User user);

    //월별 저축 금액 리턴
    @Query("select coalesce(sum(t.savedAmount),0)  from Target t "+
            "where :month = month(t.startDate) "+
            "and :month = month(t.endDate) "+
            "and t.user = :user")
    int getAllSavedAmountByMonth(@Param("month")int month, User user);

}
