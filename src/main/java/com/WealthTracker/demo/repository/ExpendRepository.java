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

public interface ExpendRepository extends JpaRepository<Expend, Long> {
    List<Expend> findAllByUser(User user);

    @Query("SELECT e FROM Expend e WHERE e.expendId = :expendId")
    Optional<Expend> findByExpendId(@Param("expendId") Long expendId);

    //카테고리도 같이 가져오기
    @Query("SELECT e FROM Expend e JOIN FETCH e.categoryExpend WHERE e.user = :user")
    List<Expend> findAllByUserWithCategory(@Param("user") User user);

    //날짜로 최신순 정렬하여 5개 가져오기
    @Query("select e from Expend e order by e.expendDate desc")
    Optional<List<Expend>> findRecentExpend(Pageable pageable);

    //이번 달 주차별 지출 총액 리턴
    @Query("select CAST(FLOOR(DAY(e.expendDate)-1)/7 + 1 AS INTEGER) AS weekNum, " +
            " SUM(e.cost) AS totalCost " +
            "from Expend e " +
            "where e.user = :user " +
            " and MONTH(e.expendDate) = MONTH(CURRENT_DATE) " +
            "group by CAST((FLOOR(DAY(e.expendDate) - 1) / 7) + 1 AS INTEGER)"
    )
    List<Object[]> getTotalExpendThisMonth(@Param("user") User user);

    //저번 달 주차별 지출 총액 리턴
    @Query("select CAST(FLOOR(DAY(e.expendDate)-1)/7 + 1 AS INTEGER) AS weekNum, " +
            " SUM(e.cost) AS totalCost " +
            "from Expend e " +
            "where e.user = :user " +
            " and MONTH(e.expendDate) = MONTH(CURRENT_DATE) - 1 " +
            "group by CAST((FLOOR(DAY(e.expendDate) - 1) / 7) + 1 AS INTEGER)"
    )
    List<Object[]> getTotalExpendLastMonth(@Param("user") User user);

    //지출내역 삭제
    void deleteById(Long expendId);

}
