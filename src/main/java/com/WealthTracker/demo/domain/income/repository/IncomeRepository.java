package com.WealthTracker.demo.domain.income.repository;

import com.WealthTracker.demo.domain.income.entity.Income;
import com.WealthTracker.demo.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income,Long> {
    List<Income> findAllByUser(User user);

    @Query("select i from Income i where i.incomeId = :incomeId")
    Optional<Income> findByIncomeId(@Param("incomeId")Long incomeId);

    //카테고리도 같이 가져오기
    @Query("select i from Income i join fetch i.categoryIncome where i.user = :user")
    List<Income> findAllByUserWithCategory(@Param("user")User user);

    //날짜로 최신순 정렬 5개
    @Query("select i from Income i order by i.incomeDate desc")
    Optional<List<Income>> findRecentIncome(Pageable pageable);

    //수입 내역 삭제
    void deleteById(Long incomeId);


    //이번 달 수입 총 내역
    @Query("select i from Income i "+
            "where month(i.incomeDate) = :month and i.user = :user "+
            "order by i.incomeDate desc")
    List<Income>findAllByIncomeDate(@Param("user")User user,@Param("month")int month);
}
