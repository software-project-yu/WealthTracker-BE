package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Expend;
import com.WealthTracker.demo.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface ExpendRepository extends JpaRepository<Expend,Long> {
    List<Expend> findAllByUser(User user);

    Optional<Expend> findByExpendId(Long expendId);

    //날짜로 최신순 정렬하여 5개 가져오기
    @Query("select e from Expend e "+
            "where e.user =: user order by e.expendDate desc"
    )
    List<Expend> findRecentExpend(@Param("user")User user, Pageable pageable);

    //이번 달 주차별 지출 총액 리턴
    @Query("select WEEK(e.expendDate),SUM(e.cost) from Expend e "+
            "where e.user = :user and MONTH(e.expendDate) = MONTH(CURRENT_DATE) "+
            "group by WEEK(e.expendDate)"
    )
    List<Object[]> getTotalExpendThisMonth(@Param("user")User user);

    //저번 달 주차별 지출 총액 리턴
    @Query("select WEEK(e.expendDate),SUM(e.cost) from Expend e "+
            "where e.user = :user and MONTH(e.expendDate) = MONTH(CURRENT_DATE) - 1 "+
            "group by WEEK(e.expendDate)")
    List<Object[]> getTotalExpendLastMonth(@Param("user")User user);

}
