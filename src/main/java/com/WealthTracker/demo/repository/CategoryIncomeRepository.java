package com.WealthTracker.demo.repository;

import com.WealthTracker.demo.domain.Category_income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryIncomeRepository extends JpaRepository<Category_income,Long> {
    //카테고리명 찾기
    @Query("select ci.categoryIncome.categoryName from CategoryIncome ci where ci.income.incomeId = :incomeId")
    Optional<String> findCategoryNameByIncomeId(@Param("incomeId")Long incomeId);
}
