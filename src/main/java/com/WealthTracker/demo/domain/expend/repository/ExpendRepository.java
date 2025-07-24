package com.WealthTracker.demo.domain.expend.repository;

import com.WealthTracker.demo.domain.expend.dto.ExpendDateResponseDTO;
import com.WealthTracker.demo.domain.expend.dto.ExpendWeekCompareDTO;
import com.WealthTracker.demo.domain.expend.entity.Expend;
import com.WealthTracker.demo.domain.user.entity.User;
import com.WealthTracker.demo.domain.category.enums.Category_Expend;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpendRepository extends JpaRepository<Expend, Long> {
    List<Expend> findAllByUser(User user);

    @Query("SELECT e FROM Expend e WHERE e.expendId = :expendId")
    Optional<Expend> findByExpendId(@Param("expendId") Long expendId);

    //카테고리도 같이 가져오기
    @Query("SELECT e FROM Expend e JOIN FETCH e.categoryExpend WHERE e.user = :user")
    List<Expend> findAllByUserWithCategory(@Param("user") User user);

    //날짜로 최신순 정렬하여 5개 가져오기
    @Query("select e from Expend e where e.user = :user " +
           "order by e.expendDate desc")
    Optional<List<Expend>> findRecentExpend(Pageable pageable, User user);

    //이번 달 최신 지출 내역 2개
    @Query("SELECT e FROM Expend e WHERE e.categoryExpend.categoryName = :category AND e.user = :user " +
           "and month (e.expendDate) = month (current_date) " +
           "and year (e.expendDate) = year (current_date) " +
           " ORDER BY e.expendDate DESC")
    List<Expend> findRecentExpend(User user, Category_Expend category, Pageable pageable);

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

    @Query(value = """
                SELECT
                    weekNum,
                    SUM(CASE WHEN monthType = 'this' THEN totalCost ELSE 0 END) AS thisMonthTotalCost,
                    SUM(CASE WHEN monthType = 'prev' THEN totalCost ELSE 0 END) AS prevMonthTotalCost
                FROM (
                      (
                    SELECT
                        WEEK(e.expendDate, 2)
                          - WEEK(DATE_SUB(e.expendDate, INTERVAL DAYOFMONTH(e.expendDate)-1 DAY), 2) + 1 AS weekNum,
                        SUM(e.cost) AS totalCost,
                        'this' AS monthType
                    FROM expend e
                    WHERE e.userId = :userId
                      AND YEAR(e.expendDate) = YEAR(CURRENT_DATE)
                      AND MONTH(e.expendDate) = MONTH(CURRENT_DATE)
                    GROUP BY WEEK(e.expendDate, 2)
                          - WEEK(DATE_SUB(e.expendDate, INTERVAL DAYOFMONTH(e.expendDate)-1 DAY), 2) + 1
                    )
                    UNION ALL
                    (
                    SELECT
                        WEEK(e.expendDate, 2)
                          - WEEK(DATE_SUB(e.expendDate, INTERVAL DAYOFMONTH(e.expendDate)-1 DAY), 2) + 1 AS weekNum,
                        SUM(e.cost) AS totalCost,
                        'prev' AS monthType
                    FROM expend e
                    WHERE e.userId = :userId
                      AND (
                            (MONTH(CURRENT_DATE) = 1 AND MONTH(e.expendDate) = 12 AND YEAR(e.expendDate) = YEAR(CURRENT_DATE) - 1)
                         OR (MONTH(CURRENT_DATE) != 1 AND MONTH(e.expendDate) = MONTH(CURRENT_DATE) - 1 AND YEAR(e.expendDate) = YEAR(CURRENT_DATE))
                      )
                    GROUP BY WEEK(e.expendDate, 2)
                          - WEEK(DATE_SUB(e.expendDate, INTERVAL DAYOFMONTH(e.expendDate)-1 DAY), 2) + 1
                    )
                ) AS union_table
                GROUP BY weekNum
                ORDER BY weekNum ASC
            """, nativeQuery = true)
    List<ExpendWeekCompareDTO> getExpendWeekCompare(@Param("userId") Long userId);

    //지출내역 삭제
    void deleteById(Long expendId);

    //월별 지출내역 리스트 반환 - 날짜는 상관없음
    @Query("select e from Expend e " +
           "where e.user = :user and MONTH(e.expendDate) = :month " +
           "order by e.expendDate desc")
    @Timed(value = "repository.findAllByExpendDate", description = "Time spent on repository#findAllByExpendDate")
    List<Expend> findAllByExpendDate(@Param("user") User user, @Param("month") int month);

    //카테고리에 따른 지출 합계
    @Query("SELECT COALESCE(SUM(e.cost), 0) FROM Expend e " +
           "WHERE e.user = :user " +
           "AND e.categoryExpend.categoryName = :categoryName " +
           "AND e.expendDate BETWEEN :weekStart AND :weekEnd")
    Long amountByCategory(
            @Param("user") User user,
            @Param("categoryName") Category_Expend categoryName,
            @Param("weekStart") LocalDateTime weekStart,
            @Param("weekEnd") LocalDateTime weekEnd
    );

    //지출 업데이트 횟수-가장최근 지출 내역 기록 시간과 일치 로직을 통해 구현
    @Query("SELECT MAX(e.createdAt) FROM Expend e WHERE e.user = :user")
    LocalDateTime findLatestExpend(@Param("user") User user);


    //이번 달카테고리별 지출 총액 가져오기
    @Query("select coalesce(sum (e.cost),0) from Expend e " +
           "where e.user =:user " +
           "and e.categoryExpend.categoryName= :categoryName " +
           "and month (e.expendDate) = month (current_date) " +
           "and year (e.expendDate) = year (current_date)"
    )
    Long thisMonthAmountByCategory(@Param("user") User user, @Param("categoryName") Category_Expend categoryName);

    //저번 달 카테고리별 지출 총액 가져오기
    @Query("select coalesce(sum (e.cost),0) from Expend e " +
           "where e.user =:user " +
           "and e.categoryExpend.categoryName = :categoryName " +
           "and month (e.expendDate) = month (current_date) - 1 " +
           "and year (e.expendDate) = year (current_date ) " +
           "or (month (current_date ) = 1 and month (e.expendDate) = 12 and year(e.expendDate) = year (current_date) - 1)"
    )
    Long prevMonthAmountByCategory(@Param("user") User user, @Param("categoryName") Category_Expend categoryName);


    //2주단위로 일별 총지출 금액
    @Query("select coalesce( sum (e.cost) , 0), e.expendDate from Expend e " +
           "where e.expendDate between :startDate and :endDate " +
           "and e.user = :user " +
           "group by e.expendDate " +
           "order by e.expendDate")
    List<Object[]> findExpendTotalByDateRange(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //최근 수정 날짜
    @Query("select max(e.updateDate) from Expend e where e.user = :user")
    LocalDateTime findLatestUpdateDate(@Param("user") User user);

}
